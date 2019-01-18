/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global self, Notification, caches */

self.addEventListener('install', function (event) {
// Preloading a Cache
    var deps = ['/','/site.webmanifest'];

    event.waitUntil(
            caches.open('my-cache-v1').then(cache => cache.addAll(deps)),
            );
});

//Responding from Cache
self.addEventListener('fetch', function(event) {
    event.respondWith(
        caches.match(event.request).then(function(response) {
            return response || fetch(event.request);
        })
    );
});
/*self.addEventListener('fetch', function (event) {
    event.respondWith(
            cache.match(event.request)
            .then(response => response || fetch(event.request))
            );
});*/
/*Gitting Permission*/
function askPermission() {
  return new Promise(function(resolve, reject) {
    const permissionResult = Notification.requestPermission(function(result) {
      resolve(result);
    });

    if (permissionResult) {
      permissionResult.then(resolve, reject);
    }
  })
  .then(function(permissionResult) {
    if (permissionResult !== 'granted') {
      throw new Error('We weren\'t granted permission.');
    }
  });
}
/*Receiving push Events*/
self.addEventListener('push', function (event) {
    event.waitUntil(
            self.registration.showNotification('Maweed Website', {
                body: 'Welcome Guest'
            })
            );
});



/*Subscribe a User with PushManager*/
function subscribeUserToPush() {
  return navigator.serviceWorker.register('service-worker.js')
  .then(function(registration) {
    const subscribeOptions = {
      userVisibleOnly: true,
      applicationServerKey: urlBase64ToUint8Array(
        'BEl62iUYgUivxIkv69yViEuiBIa-Ib9-SkvMeAtA3LFgDzkrxZJjSgSnfckjBJuBkr3qBUYIHBQFLXYp5Nksh8U'
      )
    };

    return registration.pushManager.subscribe(subscribeOptions);
  })
  .then(function(pushSubscription) {
    console.log('Received PushSubscription: ', JSON.stringify(pushSubscription));
    return pushSubscription;
  });
}
/*navigator.serviceWorker.ready.then(registration => registration.pushManager.subscribe()).then(subscription => fetch('/api/save-endpoint', {
            method: 'POST',
            headers: { 'Content-Type: application/json' },
            body: JSON.stringify(subscription)
})).then(res => );*/
