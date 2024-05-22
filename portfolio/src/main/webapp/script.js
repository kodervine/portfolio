// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

const addToPortfolioPage = async () => {
    try {
    const authResponse = await fetch("/account");

      const authData = await authResponse.text();
      console.log(authData)
    const maxComments = document.getElementById('max-comments').value;

    const response = await fetch(`/data?maxComments=${maxComments}`);
    if (!response.ok) {
      throw new Error('Failed to fetch data');
    }

    const data = await response.json();
    const commentsContainer = document.getElementById("comments-container");
    commentsContainer.innerHTML = '';

    data.forEach(comment => {
        const pTag = document.createElement("p");
        pTag.innerText = comment;
        pTag.classList.add('comment'); 
        commentsContainer.appendChild(pTag);
    });
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }
  

  const deleteComments = async () => {
    try {
    const maxComments = document.getElementById('max-comments').value;

    const deleteResponse = await fetch("/delete-data", {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
      });
  
      if (!deleteResponse.ok) {
        console.log(deleteResponse);
        throw new Error('Failed to delete comments data');
      }  

      if (deleteResponse.ok) {
    await addToPortfolioPage()        
      }
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  }
