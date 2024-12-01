const productCardsContainer = document.getElementById('productCards');
let productCount = orderDetails.length;
document.getElementById('productCount').value = productCount;
// Tao the product
const createProductCard = (count, orderDetail) => {
    // Lấy dữ liệu từ orderDetail
    const productName = orderDetail.name || "Choose product";
    const memoryId = orderDetail.productVariantDetail.memory.id;
    const memoryName = orderDetail.productVariantDetail.memory.name || "Choose memory";
    const colorId = orderDetail.productVariantDetail.color.id;
    const colorName = orderDetail.productVariantDetail.color.name || "Choose color";
    const quantity = orderDetail.quantity || 1;
    const price = orderDetail.price || "";

    // Tạo các tùy chọn
    const productOptions = `
        <option value="" ${!productName ? "selected" : ""}>Choose product</option>
        <option value="${orderDetail.productVariantDetail.id}" ${productName ? "selected" : ""}>${productName}</option>
    `;

    const memoryOptions = `
        <option value="" ${!memoryId ? "selected" : ""}>Choose memory</option>
        <option value="${memoryId}" ${memoryName ? "selected" : ""}>${memoryName}</option>
    `;

    const colorOptions = `
        <option value="" ${!colorId ? "selected" : ""}>Choose color</option>
        <option value="${colorId}" ${colorName ? "selected" : ""}>${colorName}</option>
    `;

    // Trả về HTML với các tùy chọn đã được chọn sẵn
    return `
        <div class="col-md-6" id="product-${count}">
            <div class="card mt-4">
                <div class="card-body">
                    <h5 class="card-title">Product ${count}</h5>
                    <div class="mb-3">
                        <label for="productName-${count}" class="form-label">Product</label>
                        <select class="form-control product-select" id="productName-${count}" data-id="${count}" name="productName[${count}]" readonly="">
                            ${productOptions}
                        </select>
                        <input type="hidden" name="productName[${count}]" value="${orderDetail.id}">
                    </div>
                    <div class="mb-3">
                        <label for="quantity-${count}" class="form-label">Quantity</label>
                        <input type="number" class="form-control quantity-input" id="quantity-${count}" min="1" placeholder="Quantity" name="quantity[${count}]" value="${quantity}" required>
                    </div>
                    <div class="mb-3">
                        <label for="memory-${count}" class="form-label">Memory</label>
                        <select class="form-control memory-select" id="memory-${count}" data-id="${count}" name="memory[${count}]" disabled>
                            ${memoryOptions}
                        </select>
                        <input type="hidden" name="memory[${count}]" value="${memoryId}">
                    </div>
                    <div class="mb-3">
                        <label for="color-${count}" class="form-label">Color</label>
                        <select class="form-control color-select" id="color-${count}" data-id="${count}" name="color[${count}]" disabled>
                            ${colorOptions}
                        </select>
                        <input type="hidden" name="color[${count}]" value="${colorId}">
                    </div>
                    <div class="mb-3">
                        <label for="price-${count}" class="form-label">Price</label>
                        <input type="text" class="form-control" id="price-${count}" name="price[${count}]" value="${price}" disabled>
                        <input type="hidden" class="form-control" id="price-${count}-hidden" name="price[${count}]" value="${price}">
                    </div>
                </div>
            </div>
        </div>
    `;
};

// Hàm định dạng giá tiền với dấu phân cách và hậu tố "đ"
const formatPrice = (price) => {
    return new Intl.NumberFormat('vi-VN').format(price) + " đ";
};

// Hàm bỏ định dạng giá để chuyển thành số thực
const parsePrice = (formattedPrice) => {
    return parseFloat(formattedPrice.replace(/\./g, '').replace(' đ', ''));
};

// Hàm cập nhật tổng giá theo số lượng
const updatePrice = (count) => {
    const quantityInput = document.getElementById(`quantity-${count}`);
    const priceInput = document.getElementById(`price-${count}`);
    const hiddenPriceInput = document.getElementById(`price-${count}-hidden`);

    // Lấy giá trị đơn giá từ hiddenPriceInput
    const unitPrice = parsePrice(hiddenPriceInput.value) / parseInt(quantityInput.value, 10);

    // Lắng nghe sự thay đổi của số lượng
    quantityInput.addEventListener('input', () => {
        const newQuantity = parseInt(quantityInput.value, 10) || 1; // Số lượng mới
        const newTotalPrice = unitPrice * newQuantity; // Giá mới = đơn giá * số lượng mới

        // Cập nhật giá mới lên giao diện
        priceInput.value = formatPrice(newTotalPrice); // Hiển thị giá đã định dạng
        hiddenPriceInput.value = newTotalPrice.toFixed(2); // Lưu giá trị số thực vào input ẩn
    });
};

// Lặp qua orderDetails và thêm sự kiện lắng nghe thay đổi của quantity
orderDetails.forEach((orderDetail, index) => {
    const productCardHTML = createProductCard(index + 1, orderDetail);
    productCardsContainer.innerHTML += productCardHTML;

    // Định dạng và cập nhật giá ban đầu
    const priceInput = document.getElementById(`price-${index + 1}`);
    priceInput.value = formatPrice(orderDetail.price || 0);

    // Tính và cập nhật giá khi quantity thay đổi
    updatePrice(index + 1);
});
