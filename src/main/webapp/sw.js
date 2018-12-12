/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*self.addEventListener('fetch', (event) => {});*/

/*importScripts('/cache-polyfill.js');

self.addEventListener('install', function(e) {
 e.waitUntil(
   caches.open('airhorner').then(function(cache) {
     return cache.addAll([
       '/',
       '/index.xhtml',
       '/index.xhtml?homescreen=1',
       '/?homescreen=1',
       '/resources/indexcss.css',
       /*'/javascript/main.min.js',*/
      /* '/sounds/airhorn.mp3'*/
    /* ]);*/
  /* })*/
/* );*/
/*});*/

self.addEventListener('fetch', function(event) {
 console.log(event.request.url);

 event.respondWith(
   caches.match(event.request).then(function(response) {
     return response || fetch(event.request);
   })
 );
});