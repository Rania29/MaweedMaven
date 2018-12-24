/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global self */

self.addEventListener('install', function (event) {
// Preloading a Cache
    var deps = ['/', '/site.webmanifest'];

    event.waitUntil(
            caches.open('my-cache-v1').then(cache => cache.addAll(deps)),
            );
});

//Responding from Cache
self.addEventListener('fetch', function (event) {
    event.respondWith(
            cache.match(event.request)
            .then(response => response || fetch(event.request))
            );
});

/*Receiving push Events*/
self.addEventListener('push', function (event) {
    event.waitUntil(
            self.registration.showNotification('Maweed Website', {
                body: 'Welcome Guest'
            })
            );
});


/*Gitting Permission*/
/*navigator.serviceWorker.ready.then(registration => registration.pushManager.subscribe()).then(subscription => fetch('/api/save-endpoint', {
            method: 'POST',
            headers: { 'Content-Type: application/json' },
            body: JSON.stringify(subscription)
})).then(res => );*/
