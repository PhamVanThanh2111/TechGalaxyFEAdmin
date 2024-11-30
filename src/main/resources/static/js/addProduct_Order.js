const addProductButton = document.getElementById('addProduct');
const productCardsContainer = document.getElementById('productCards');

let productCount = 0; // Biến đếm số lượng sản phẩm đã được thêm
let activeProductIds = []; // Mảng lưu trữ các ID của sản phẩm đang có

// Hàm cập nhật số lượng sản phẩm
const updateProductCount = () => {
    const productCountInput = document.getElementById('productCount');
    const totalProducts = productCardsContainer.querySelectorAll('.card').length;
    productCountInput.value = totalProducts;
};

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
                        <select class="form-control product-select" id="productName-${count}" data-id="${count}" name="productName[${count}]">
                            <option value="">Choose product</option>
                            ${options}
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="quantity-${count}" class="form-label">Quantity</label>
                        <input type="number" class="form-control quantity-input" id="quantity-${count}" min="1" placeholder="Quantity" name="quantity[${count}]" value="1" required>
                        <label for="quantity-${count}" class="">Remaining: 0</label>
                    </div>
                    <div class="mb-3">
                        <label for="memory-${count}" class="form-label">Memory</label>
                        <select class="form-control memory-select" id="memory-${count}" data-id="${count}" name="memory[${count}]">
                            <option value="">Choose memory</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="color-${count}" class="form-label">Color</label>
                        <select class="form-control color-select" id="color-${count}" data-id="${count}" name="color[${count}]">
                            <option value="">Choose color</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="price-${count}" class="form-label">Price</label>
                        <input type="text" class="form-control" id="price-${count}" name="price[${count}]" disabled>
                        <input type="hidden" class="form-control" id="price-${count}-hidden" name="price[${count}]">
                    </div>
                    <button type="button" class="btn btn-danger btn-icon-split remove-card text-center" data-id="${count}">
                        <span class="icon text-white-50">
                            <i class="fa-solid fa-minus"></i>
                        </span>
                        <span class="text">Remove</span>
                    </button>
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

    productCount = newCount - 1; // Cập nhật lại biến productCount
};

// Xử lý sự kiện xóa sản phẩm
productCardsContainer.addEventListener('click', (event) => {
    // Tìm phần tử nút cha có class "remove-card" gần nhất
    const button = event.target.closest('.remove-card');
    if (button) {
        const cardId = button.getAttribute('data-id'); // Lấy ID của thẻ
        const cardElement = document.getElementById(`product-${cardId}`);
        if (cardElement) {
            cardElement.remove(); // Xóa thẻ sản phẩm
            console.log(`Product ${cardId} removed`);
            activeProductIds = activeProductIds.filter(id => id !== parseInt(cardId)); // Cập nhật lại mảng activeProductIds
            updateProductOrder(); // Cập nhật lại thứ tự sau khi xóa
            updateProductCount(); // Cập nhật số lượng
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
                document.getElementById(`price-${count}-hidden`).value = price.toLocaleString();
            }
        }
    }
};

// Cập nhật remaining dựa trên các lựa chọn
const updateRemaining = (count) => {
    const productId = document.getElementById(`productName-${count}`).value;
    const memoryId = document.getElementById(`memory-${count}`).value;
    const colorId = document.getElementById(`color-${count}`).value;

    if (productId && memoryId && colorId) {
        const product = productVariants.find(p => p.id === productId);
        if (product) {
            const memoryData = product.productVariantDetails[0].memories[memoryId];
            const colorData = memoryData.find(color => color.colorId === colorId);
            if (colorData) {
                const remainingLabel = document.querySelector(`#quantity-${count} ~ label`);
                if (remainingLabel) {
                    remainingLabel.textContent = `Remaining: ${colorData.quantity}`;
                }
            }
        }
    }
};

// Xử lý khi thay đổi sản phẩm
const handleProductChange = (productId, count) => {
    const product = productVariants.find(p => p.id === productId);
    if (product && product.productVariantDetails.length > 0) {
        const productDetail = product.productVariantDetails[0]; // Lấy phần tử đầu tiên
        const memorySelect = document.getElementById(`memory-${count}`);
        const colorSelect = document.getElementById(`color-${count}`);

        // Cập nhật các tùy chọn bộ nhớ (memory)
        const memories = productDetail.memories || {};
        memorySelect.innerHTML = createMemoryOptions(memories);

        // Gắn sự kiện khi thay đổi bộ nhớ (memory)
        memorySelect.addEventListener('change', () => {
            const selectedMemoryId = memorySelect.value;

            // Cập nhật danh sách color theo memory mới
            if (selectedMemoryId) {
                colorSelect.innerHTML = createColorOptions(memories[selectedMemoryId] || []);
            } else {
                colorSelect.innerHTML = '<option value="">Choose color</option>';
            }

            // Cập nhật giá sau khi thay đổi
            updatePrice(count);
            updateRemaining(count);
        });

        // Cập nhật danh sách màu sắc (color) mặc định theo bộ nhớ đầu tiên
        const firstMemoryId = Object.keys(memories)[0];
        if (firstMemoryId) {
            colorSelect.innerHTML = createColorOptions(memories[firstMemoryId] || []);
        }

        // Cập nhật giá ngay sau khi thay đổi product
        updatePrice(count);
        updateRemaining(count);
    } else {
        console.warn(`Product ${productId} does not have variant details or memories.`);
    }
};

// Gắn sự kiện thay đổi color
const handleColorChange = (count) => {
    const colorSelect = document.getElementById(`color-${count}`);
    if (colorSelect) {
        colorSelect.addEventListener('change', () => {
            updatePrice(count); // Gọi hàm cập nhật giá khi thay đổi màu
            updateRemaining(count); // Gọi hàm cập nhật remaining khi thay đổi màu
        });
    }
};

// Gắn sự kiện thay đổi quantity
const handleQuantityChange = (count) => {
    const quantityInput = document.getElementById(`quantity-${count}`);
    if (quantityInput) {
        quantityInput.addEventListener('input', () => {
            updatePrice(count); // Gọi hàm cập nhật giá khi thay đổi số lượng
        });
    }
};

// Xử lý khi thêm sản phẩm mới
addProductButton.addEventListener('click', () => {
    const newProductCard = createProductCard(productCount + 1); // Tạo thẻ mới với ID theo thứ tự
    productCardsContainer.insertAdjacentHTML('beforeend', newProductCard);
    productCount++;
    updateProductOrder(); // Cập nhật lại thứ tự của tất cả sản phẩm
    updateProductCount(); // Cập nhật lại số lượng sản phẩm

    // Gắn sự kiện cho productName mới
    const productSelect = document.getElementById(`productName-${productCount}`);
    if (productSelect) {
        productSelect.addEventListener('change', () => {
            handleProductChange(productSelect.value, productCount);
            updatePrice(productCount); // Cập nhật giá khi thay đổi product
        });
    } else
        console.error("Product select not found!");

    // Gắn sự kiện cho memory, color và quantity
    handleColorChange(productCount);
    handleQuantityChange(productCount);
});

