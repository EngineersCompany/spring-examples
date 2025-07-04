/**
 * 
 */
        function sendPutRequest() {
            const form = document.getElementById('putForm');
            const formData = new FormData(form);
            const data = {};
	//			id : "12",
	//			name : "xxx",
	//			price : "280",
	//			stock : "80"
	//		};
            formData.forEach((value, key) => data[key] = value);

            fetch('/maintenance/product/put/' + data.id, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            })
            .then(response => {
                if (response.ok) {
                    alert('Product updated successfully!');
                } else {
                    alert('Failed to update product.');
                }
                console.log(response);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('An error occurred.');
            });
        }
