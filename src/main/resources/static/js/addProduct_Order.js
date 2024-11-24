const addProductButton = document.getElementById('addProduct');
const productCardsContainer = document.getElementById('productCards');

let productCount = 0; // Biến đếm số lượng sản phẩm đã được thêm
let activeProductIds = []; // Mảng lưu trữ các ID của sản phẩm đang có

// Hàm ánh xạ id sang name
const getNameById = (id, list) => {
    const item = list.find(el => el.id === id);
    return item ? item.name : "Unknown";
};

// Tạo các thẻ option cho sản phẩm
const createProductOptions = () => {
    if (!Array.isArray(productVariants)) {
        console.error("Product list is not an array!");
        return '';
    }
    return productVariants.map(product =>
        `<option value="${product.id}">${product.name}</option>`
    ).join('');
};

// Tạo các thẻ option cho memory
const createMemoryOptions = (memoryData) => {
    return Object.keys(memoryData).map(memoryId =>
        `<option value="${memoryId}">${getNameById(memoryId, memories)}</option>`
    ).join('');
};

// Tạo các thẻ option cho color
const createColorOptions = (colorData) => {
    return colorData.map(color =>
        `<option value="${color.colorId}">${getNameById(color.colorId, colors)}</option>`
    ).join('');
};

// Tạo một thẻ sản phẩm
const createProductCard = (count) => {
    const options = createProductOptions();
    return `
        <div class="col-md-6" id="product-${count}">
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title">Product ${count}</h5>
                    <div class="mb-3">
                        <label for="productName-${count}" class="form-label">Product</label>
                        <select class="form-control product-select" id="productName-${count}" data-id="${count}">
                            <option value="">Choose product</option>
                            ${options}
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="quantity-${count}" class="form-label">Quantity</label>
                        <input type="number" class="form-control quantity-input" id="quantity-${count}" min="1" placeholder="Quantity">
                    </div>
                    <div class="mb-3">
                        <label for="memory-${count}" class="form-label">Memory</label>
                        <select class="form-control memory-select" id="memory-${count}" data-id="${count}">
                            <option value="">Choose memory</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="color-${count}" class="form-label">Color</label>
                        <select class="form-control color-select" id="color-${count}" data-id="${count}">
                            <option value="">Choose color</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="price-${count}" class="form-label">Price</label>
                        <input type="text" class="form-control" id="price-${count}" disabled>
                    </div>
                    <button type="button" class="btn btn-danger remove-card" data-id="${count}">Remove</button>
                </div>
            </div>
        </div>
    `;
};

// Cập nhật lại thứ tự cho các sản phẩm khi có thẻ bị xóa
const updateProductOrder = () => {
    const allProductCards = productCardsContainer.querySelectorAll('.card');
    let newCount = 1;
    activeProductIds = []; // Reset lại mảng activeProductIds

    allProductCards.forEach(card => {
        const cardId = card.querySelector('.card-title');
        if (cardId) {
            // Cập nhật lại tiêu đề của sản phẩm
            cardId.textContent = `Product ${newCount}`;

            // Lấy các input và select của thẻ
            const productSelect = card.querySelector('.product-select');
            const quantityInput = card.querySelector('.quantity-input');
            const memorySelect = card.querySelector('.memory-select');
            const colorSelect = card.querySelector('.color-select');
            const priceInput = card.querySelector('.form-control[disabled]');

            // Cập nhật lại ID cho tất cả các input
            productSelect.id = `productName-${newCount}`;
            quantityInput.id = `quantity-${newCount}`;
            memorySelect.id = `memory-${newCount}`;
            colorSelect.id = `color-${newCount}`;
            priceInput.id = `price-${newCount}`;

            // Cập nhật lại data-id cho các thẻ
            productSelect.setAttribute('data-id', newCount);
            quantityInput.setAttribute('data-id', newCount);
            memorySelect.setAttribute('data-id', newCount);
            colorSelect.setAttribute('data-id', newCount);

            // Thêm vào mảng activeProductIds để theo dõi thứ tự
            activeProductIds.push(newCount);

            newCount++;
        }
    });
};

// Xử lý sự kiện xóa sản phẩm
productCardsContainer.addEventListener('click', (event) => {
    if (event.target.classList.contains('remove-card')) {
        const cardId = event.target.getAttribute('data-id');
        const cardElement = document.getElementById(`product-${cardId}`);
        if (cardElement) {
            cardElement.remove();
            console.log(`Product ${cardId} removed`);
            activeProductIds = activeProductIds.filter(id => id !== parseInt(cardId)); // Cập nhật lại mảng activeProductIds
            updateProductOrder(); // Cập nhật lại thứ tự sau khi xóa
        }
    }
});

// Cập nhật giá dựa trên các lựa chọn
const updatePrice = (count) => {
    const productId = document.getElementById(`productName-${count}`).value;
    const memoryId = document.getElementById(`memory-${count}`).value;
    const colorId = document.getElementById(`color-${count}`).value;
    const quantity = parseInt(document.getElementById(`quantity-${count}`).value, 10) || 1;

    if (productId && memoryId && colorId) {
        const product = productVariants.find(p => p.id === productId);
        if (product) {
            const memoryData = product.productVariantDetails[0].memories[memoryId];
            const colorData = memoryData.find(color => color.colorId === colorId);
            if (colorData) {
                const price = colorData.price * quantity;
                document.getElementById(`price-${count}`).value = price.toLocaleString();
            }
        }
    }
};

// Xử lý khi thay đổi sản phẩm
const handleProductChange = (productId, count) => {
    const product = productVariants.find(p => p.id === productId);
    if (product) {
        const memorySelect = document.getElementById(`memory-${count}`);
        const colorSelect = document.getElementById(`color-${count}`);

        // Cập nhật các tùy chọn bộ nhớ (memory)
        memorySelect.innerHTML = createMemoryOptions(product.productVariantDetails[0].memories);

        // Gắn sự kiện khi thay đổi bộ nhớ (memory)
        memorySelect.addEventListener('change', () => {
            const selectedMemoryId = memorySelect.value;

            // Cập nhật danh sách color theo memory mới
            if (selectedMemoryId) {
                colorSelect.innerHTML = createColorOptions(product.productVariantDetails[0].memories[selectedMemoryId]);
            } else {
                colorSelect.innerHTML = '<option value="">Choose color</option>';
            }

            // Cập nhật giá sau khi thay đổi
            updatePrice(count);
        });

        // Cập nhật danh sách màu sắc (color) mặc định theo bộ nhớ đầu tiên
        const firstMemoryId = Object.keys(product.productVariantDetails[0].memories)[0];
        if (firstMemoryId) {
            colorSelect.innerHTML = createColorOptions(product.productVariantDetails[0].memories[firstMemoryId]);
        }
    }
};

// Xử lý khi thêm sản phẩm mới
addProductButton.addEventListener('click', () => {
    const newProductCard = createProductCard(productCount + 1); // Tạo thẻ mới với ID theo thứ tự
    productCardsContainer.insertAdjacentHTML('beforeend', newProductCard);
    productCount++;
    updateProductOrder(); // Cập nhật lại thứ tự của tất cả sản phẩm
});
