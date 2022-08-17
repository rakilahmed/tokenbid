// Utility function to perform an HTTPRequest, returns a promise with an HTTPResponse
async function sendRequest(method, path, body) {
  return await fetch(
    new Request(path, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(body),
    })
  );
}

// Add data to database
async function addUser(user) {
  return await sendRequest('POST', '/users/add', user);
}

async function loginUser(user) {
  return await sendRequest('POST', '/users/login', user);
}

async function addItem(item) {
  return await sendRequest('POST', '/items/add', item);
}

async function addAuction(auction) {
  return await sendRequest('POST', '/auctions/add', auction);
}

async function addBid(bid) {
  return await sendRequest('POST', '/bids/add', bid);
}

// Get data from the database
async function getUser(userId) {
  const response = await sendRequest('GET', '/users/' + userId);
  if (!response.ok) return null;
  return response.json();
}

async function getItem(itemId) {
  const response = await sendRequest('GET', '/items/' + itemId);
  if (!response.ok) return null;
  return response.json();
}

async function getAuction(auctionId) {
  const response = await sendRequest('GET', '/auctions/' + auctionId);
  if (!response.ok) return null;
  return response.json();
}

async function getBid(bidId) {
  const response = await sendRequest('GET', '/bids/' + bidId);
  if (!response.ok) return null;
  return response.json();
}

async function getItemsByCategory(category) {
  // TODO add endpoint to query by category
}

async function getAllItems() {
  const response = await sendRequest('GET', '/items/all');
  if (!response.ok) return null;
  return response.json();
}

// Update data on database
async function updateUser(user) {
  console.log(user);
  return await sendRequest('PUT', '/users/' + user.userId, user);
}

async function updateItem(item) {
  return await sendRequest('PUT', '/items/' + item.itemId, item);
}

async function updateAuction(auction) {
  return await sendRequest('PUT', '/auctions/' + auction.auctionId, auction);
}

async function updateBid(bid) {
  return await sendRequest('PUT', '/bids/' + bid.bidId, bid);
}

module.exports = {
  addUser,
  loginUser,
  addItem,
  addAuction,
  addBid,
  getUser,
  getItem,
  getAuction,
  getBid,
  getItemsByCategory,
  getAllItems,
  updateUser,
  updateItem,
  updateAuction,
  updateBid,
};
