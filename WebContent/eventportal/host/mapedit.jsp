<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>イベント作成・マップ編集</title>
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <style>
        /* 基本スタイル (host_event_create.jspより) */
        .container { max-width: 900px; margin: 0 auto; padding: 20px; font-family: Arial, sans-serif; }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box;
        }

        /* 地図エリアのスタイル (mapedit.jspより) */
        #map { height: 400px; width: 100%; border: 2px solid #ddd; border-radius: 5px; margin: 10px 0; }
        .search-section { background: #e8f4f8; padding: 15px; border-radius: 5px; margin-bottom: 10px; }
        .search-box { display: flex; gap: 10px; }
        .btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-primary { background: #007bff; color: white; }

        /* プレビュー・装飾 */
        .preview { margin-top: 10px; max-width: 200px; }
        .form-actions { text-align: center; margin-top: 30px; }
        .form-actions button { margin: 0 10px; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
        .submit-btn { background-color: #28a745; color: white; border: none; }

        /* モーダル (ピン編集用) */
        .modal { display: none; position: fixed; z-index: 1000; left: 0; top: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.4); }
        .modal-content { background: white; margin: 10% auto; padding: 20px; width: 400px; border-radius: 5px; }
    </style>
</head>
<body>

<div class="container">
    <h1>イベント作成  会場マップ設定</h1>

    <form id="mapForm" action="MapAction" method="post">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="mapId" value="${map.mapId}">

    <%-- returnToパラメータを保持 --%>
    <c:if test="${not empty returnTo}">
        <input type="hidden" name="returnTo" value="${returnTo}">
    </c:if>

        <div class="form-group">
            <label for="event_name">イベント名</label>
            <input type="text" id="event_name" name="event_name" placeholder="例: マヤ文明鎮魂祭" required>
        </div>

        <div style="display: flex; gap: 10px; flex-wrap: wrap;">
            <div class="form-group">
                <label>開催日</label>
                <select name="event_year" style="width: auto;">
                    <option value="2025">2025年</option>
                    <option value="2026">2026年</option>
                </select>
                <select name="event_month" style="width: auto;">
                    <c:forEach var="i" begin="1" end="12"><option value="${i}">${i}月</option></c:forEach>
                </select>
                <select name="event_day" style="width: auto;">
                    <c:forEach var="i" begin="1" end="31"><option value="${i}">${i}日</option></c:forEach>
                </select>
            </div>
        </div>

        <hr>

        <h3>📍 会場場所の設定</h3>
        <div class="search-section">
            <label>住所で地図を検索</label>
            <div class="search-box">
                <input type="text" id="addressSearch" placeholder="例: 東京都千代田区...">
                <button type="button" class="btn btn-primary" onclick="searchAddress()">検索</button>
            </div>
            <small>※地図をクリックしてピンを立てると住所が自動入力されます</small>
        </div>

        <div id="map"></div>

        <div class="form-group">
            <label for="postalCode">郵便番号</label>
            <input type="text" id="postalCode" name="postalCode" placeholder="123-4567" required>
        </div>

        <div class="form-group">
            <label for="prefecture">都道府県</label>
            <input type="text" id="prefecture" name="prefecture" readonly>
        </div>

        <div class="form-group">
            <label for="city">市区町村</label>
            <input type="text" id="city" name="city" required>
        </div>

        <div class="form-group">
            <label for="street">町名・番地</label>
            <input type="text" id="street" name="street" required>
        </div>

        <hr>

        <div class="form-group">
            <label>会場マップ画像（アップロード）</label>
            <input type="file" name="eventMapImage" accept="image/*">
        </div>

        <div class="form-group">
            <label for="maxcount">最大人数</label>
            <input type="number" id="maxcount" name="maxcount" placeholder="500">
        </div>

        <input type="hidden" id="lat" name="latitude">
        <input type="hidden" id="lng" name="longitude">

        <div class="form-actions">
            <button type="button" onclick="history.back()">戻る</button>
            <button type="submit" class="submit-btn">イベントを作成する</button>
        </div>
    </form>
</div>

<div id="pinModal" class="modal">
    <div class="modal-content">
        <h3>地点情報の確認</h3>
        <p id="modalAddressText"></p>
        <button type="button" class="btn btn-primary" onclick="confirmLocation()">この場所に決定</button>
        <button type="button" onclick="closeModal()">キャンセル</button>
    </div>
</div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    let map, marker;
    let tempLat, tempLng;

    document.addEventListener('DOMContentLoaded', function() {
        // 地図初期化
        map = L.map('map').setView([35.6809, 139.7673], 13);
        L.tileLayer('https://cyberjapandata.gsi.go.jp/xyz/std/{z}/{x}/{y}.png', {
            attribution: '&copy; <a href="https://maps.gsi.go.jp/development/ichiran.html">国土地理院</a>'
        }).addTo(map);

        // クリックイベント
        map.on('click', function(e) {
            tempLat = e.latlng.lat;
            tempLng = e.latlng.lng;
            openPinModal(tempLat, tempLng);
        });
    });

    function openPinModal(lat, lng) {
        // 逆ジオコーディングで住所取得を試みる
        fetch(`https://mreversegeocoder.gsi.go.jp/reverse-geocoder/LonLatToAddress?lat=${lat}&lon=${lng}`)
            .then(res => res.json())
            .then(data => {
                const addr = data.results ? "地点を取得しました" : "不明な地点";
                document.getElementById('modalAddressText').innerText = addr;
                document.getElementById('pinModal').style.display = 'block';
            });
    }

    function confirmLocation() {
        // マーカーを置く
        if (marker) map.removeLayer(marker);
        marker = L.marker([tempLat, tempLng]).addTo(map);

        // フォームに座標をセット
        document.getElementById('lat').value = tempLat;
        document.getElementById('lng').value = tempLng;

        // 注意：本来はここで住所を分解して各入力欄に入れるロジックを組みます
        // 今回は簡易的に「町名・番地」に座標を入れる例
        document.getElementById('street').value = `緯度:${tempLat.toFixed(4)} 経度:${tempLng.toFixed(4)}`;

        closeModal();
    }

    function closeModal() {
        document.getElementById('pinModal').style.display = 'none';
    }

    function searchAddress() {
        const address = document.getElementById('addressSearch').value;
        if (!address) return alert('住所を入力してください');

        fetch(`https://msearch.gsi.go.jp/address-search/AddressSearch?q=${encodeURIComponent(address)}`)
            .then(res => res.json())
            .then(data => {
                if (data.length > 0) {
                    const lonLat = data[0].geometry.coordinates;
                    map.setView([lonLat[1], lonLat[0]], 16);
                } else {
                    alert('見つかりませんでした');
                }
            });
    }
</script>

</body>
</html>