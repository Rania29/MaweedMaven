/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/* global self, Notification, caches, btnAdd */

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

/*New part to try*/
/*Listen for beforeinstallprompt*/
let deferredPrompt;

window.addEventListener('beforeinstallprompt', (e) => {
  // Prevent Chrome 67 and earlier from automatically showing the prompt
  e.preventDefault();
  // Stash the event so it can be triggered later.
  deferredPrompt = e;
});

/*Notify the user your app can be installed*/
window.addEventListener('beforeinstallprompt', (e) => {
  // Prevent Chrome 67 and earlier from automatically showing the prompt
  e.preventDefault();
  // Stash the event so it can be triggered later.
  deferredPrompt = e;
  // Update UI notify the user they can add to home screen
  btnAdd.style.display = 'block';
});

/*Show the prompt*/
btnAdd.addEventListener('click', (e) => {
  // hide our user interface that shows our A2HS button
  btnAdd.style.display = 'none';
  // Show the prompt
  deferredPrompt.prompt();
  // Wait for the user to respond to the prompt
  deferredPrompt.userChoice
    .then((choiceResult) => {
      if (choiceResult.outcome === 'accepted') {
        console.log('User accepted the A2HS prompt');
      } else {
        console.log('User dismissed the A2HS prompt');
      }
      deferredPrompt = null;
    });
});





/*Gitting Permission*/
/*function askPermission() {
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
/*self.addEventListener('push', function (event) {
    event.waitUntil(
            self.registration.showNotification('Maweed Website', {
                body: 'Welcome Guest'
            })
            );
});*/



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
