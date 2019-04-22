/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* global x */

function myFunction(x) {
                x.classList.toggle("change");
                document.getElementById("myDropdown").classList.toggle("menu");
            }

/********sticky menu*********/

// When the user scrolls the page, execute myFunction 
window.onscroll = function() {myFunction1();};

// Get the header
var header = document.getElementById("myHeader");

// Get the offset position of the navbar
var sticky = header.offsetTop;

// Add the sticky class to the header when you reach its scroll position. Remove "sticky" when you leave the scroll position
function myFunction1() {
  if (window.pageYOffset > sticky) {
    header.classList.add("sticky");
  } else {
    header.classList.remove("sticky");
  }
}