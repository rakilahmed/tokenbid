(()=>{var t={925:t=>{async function e(t,e,n){return await fetch(new Request(e,{method:t,headers:{"Content-Type":"application/json"},body:JSON.stringify(n)}))}t.exports={addUser:async function(t){return await e("POST","/users/add",t)},loginUser:async function(t){return await e("POST","/users/login",t)},addItem:async function(t){return await e("POST","/items/add",t)},addAuction:async function(t){return await e("POST","/auctions/add",t)},addBid:async function(t){return await e("POST","/bids/add",t)},getUser:async function(t){const n=await e("GET","/users/"+t);return n.ok?n.json():null},getItem:async function(t){const n=await e("GET","/items/"+t);return n.ok?n.json():null},getAuction:async function(t){const n=await e("GET","/auctions/"+t);return n.ok?n.json():null},getBid:async function(t){const n=await e("GET","/bids/"+t);return n.ok?n.json():null},getItemsByCategory:async function(t){},getAllItems:async function(){const t=await e("GET","/items/all");return t.ok?t.json():null},updateUser:async function(t){return console.log(t),await e("PUT","/users/"+t.userId,t)},updateItem:async function(t){return await e("PUT","/items/"+t.itemId,t)},updateAuction:async function(t){return await e("PUT","/auctions/"+t.auctionId,t)},updateBid:async function(t){return await e("PUT","/bids/"+t.bidId,t)}}}},e={};function n(a){var o=e[a];if(void 0!==o)return o.exports;var s=e[a]={exports:{}};return t[a](s,s.exports,n),s.exports}(()=>{const t=n(925),e=t.addUser,a=t.loginUser,o=t.addItem,s=t.addAuction,i=t.addBid,d=(t.getUser,t.getItem,t.getAuction,t.getBid,t.getItemsByCategory,t.getAllItems,t.updateUser,t.updateItem,t.updateAuction,t.updateBid,document.getElementById("register-form"));d&&d.addEventListener("submit",(async function(t){t.preventDefault();const n=new FormData(t.target);let a={};a.firstName=n.get("firstName"),a.lastName=n.get("lastName"),a.username=n.get("username"),a.email=n.get("email"),a.password=n.get("password"),a.tokens=0;let o=await e(a);if(o.ok)console.log("User added!"),window.location.href="/login.html";else if(409===o.status){let t=await o.text();alert(t)}else console.log("Failed to add user")}));const r=document.getElementById("login-form");r&&r.addEventListener("submit",(async function(t){t.preventDefault();const e=new FormData(t.target);let n={};n.username=e.get("username"),n.password=e.get("password"),console.log(n);let o=await a(n);console.log(o),o.ok?(console.log("User logged in!"),window.location.href="/explore.html"):console.log("Failed to log in user")}));const c=document.getElementById("item-form");c&&c.addEventListener("submit",(async function(t){t.preventDefault();const e=new FormData(t.target);let n={userId:1};n.title=e.get("title"),n.description=e.get("description"),n.category=e.get("category"),(await o(n)).ok?console.log("Item added!"):console.log("Failed to add item")}));const u=document.getElementById("auction-form");u&&u.addEventListener("submit",(async function(t){t.preventDefault();const e=new FormData(t.target);let n=Number(e.get("length")),a=(new Date).getTime(),o=a+36e5*n,i={};i.itemId=e.get("itemId"),i.startingBid=e.get("startingBid"),i.startTime=new Date(a).toJSON(),i.endTime=new Date(o).toJSON(),(await s(i)).ok?console.log("Auction added!"):console.log("Failed to add auction")}));const l=document.getElementById("bid-form");l&&l.addEventListener("submit",(async function(t){t.preventDefault();const e=new FormData(t.target);let n={userId:1};n.auctionId=e.get("auctionId"),n.bid=e.get("bid");let a=await i(n);if(a.ok)console.log("Bid added!");else if(409===a.status){let t=await a.text();alert(t)}else console.log("Failed to add bid")}))})()})();