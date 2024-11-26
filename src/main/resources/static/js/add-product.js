let variantCount = 0;

function addVariant() {
    const variantIndex = variantCount++;
    const variantContainer = document.createElement('div');
    variantContainer.className = 'variant-container p-3 mb-3 shadow-sm';
    variantContainer.innerHTML = `
        <hr class="my-4">
        <h5 class="text-info font-weight-bold font-styles-italic">Variant ${variantIndex + 1}</h5>

        <!-- Tên Variant -->
        <div class="form-group mb-2">
            <label for="variants${variantIndex}.name" class="form-label font-weight-bold text-dark">Tên Variant</label>
            <input type="text" class="form-control form-control-user" name="variants[${variantIndex}].name" placeholder="Nhập tên variant" required>
            <small class="text-danger"></small>
        </div>

        <!-- Mô Tả -->
        <div class="form-group">
            <label for="variants${variantIndex}.description" class="form-label font-weight-bold text-dark">Mô Tả</label>
            <textarea class="form-control" name="variants[${variantIndex}].description" placeholder="Nhập mô tả" required></textarea>
            <small class="text-danger"></small>
        </div>

        <!-- Nội Dung -->
        <div class="form-group">
            <label for="variants${variantIndex}.content" class="form-label font-weight-bold text-dark">Nội Dung</label>
            <textarea class="form-control" name="variants[${variantIndex}].content" placeholder="Nhập nội dung" required></textarea>
            <small class="text-danger"></small>
        </div>

        <!-- Avatar -->
        <div class="form-group">
            <label for="variants${variantIndex}.avatar" class="form-label font-weight-bold text-dark">Avatar</label>
            <input type="file" class="form-control" name="variants[${variantIndex}].avatar" accept=".jpg, .jpeg, .png, .svg">
        </div>

        <!-- Usage Category Combobox -->
        <div class="form-group">
            <label for="variants${variantIndex}.usageCategoryId" class="form-label font-weight-bold text-dark">Usage Category</label>
            <select class="form-control" name="variants[${variantIndex}].usageCategoryId">
                ${document.getElementById('usageCategoryTemplate').innerHTML}
            </select>
            <small class="text-danger"></small>
        </div>

        <!-- Variant Details Section -->
        <div id="detailsContainer${variantIndex}" class="mt-3">
            <h6 class="font-weight-bold text-dark">Variant Details</h6>
        </div>
        <button type="button" class="btn btn-outline-secondary btn-sm mb-3" onclick="addVariantDetail(${variantIndex})">+ Thêm Detail</button>
        <button type="button" class="btn btn-danger btn-sm mb-3" onclick="removeVariant(${variantIndex})">Xoá Variant</button>
    `;
    document.getElementById('variantsContainer').appendChild(variantContainer);
}

function addVariantDetail(variantIndex) {
    const detailContainer = document.createElement('div');
    const detailIndex = document.querySelectorAll(`#detailsContainer${variantIndex} .detail-container`).length;
    detailContainer.className = 'detail-container p-2 mb-2 shadow-sm';
    detailContainer.innerHTML = `
        <hr class="my-3">
        <h6 class="text-warning font-weight-bold font-styles-italic">Detail ${detailIndex + 1}</h6>

        <!-- Memory Combobox -->
        <div class="form-group">
            <label class="form-label font-weight-bold text-dark">Memory</label>
            <select class="form-control" name="variants[${variantIndex}].details[${detailIndex}].memid">
                ${document.getElementById('memoryTemplate').innerHTML}
            </select>
            <small class="text-danger"></small>
        </div>

        <!-- Giá -->
        <div class="form-group">
            <label class="form-label font-weight-bold text-dark">Giá</label>
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].price" placeholder="Nhập giá" required>
            <small class="text-danger"></small>
        </div>

        <!-- Sale -->
        <div class="form-group">
            <label class="form-label font-weight-bold text-dark">Giảm Giá</label>
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].sale" placeholder="Nhập giảm giá" required min="0" step="0.01" max="1">
            <small class="text-danger"></small>
        </div>

        <!-- Color Section -->
        <div id="colorContainer${variantIndex}_${detailIndex}" class="mt-3">
            <h6 class="font-weight-bold text-dark">Colors</h6>
        </div>
        <button type="button" class="btn btn-outline-info btn-sm mb-2" onclick="addColor(${variantIndex}, ${detailIndex})">+ Thêm Color</button>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeDetail(${variantIndex}, ${detailIndex})">Xoá Detail</button>
    `;
    document.getElementById(`detailsContainer${variantIndex}`).appendChild(detailContainer);
}

function addColor(variantIndex, detailIndex) {
    const colorContainer = document.createElement('div');
    const colorIndex = document.querySelectorAll(`#colorContainer${variantIndex}_${detailIndex} .color-container`).length;
    colorContainer.className = 'color-container row mb-2';
    colorContainer.innerHTML = `
        <hr class="my-2">
        <div class="col-md-6">
            <label class="form-label font-weight-bold text-dark">Color</label>
            <select class="form-control" name="variants[${variantIndex}].details[${detailIndex}].colors[${colorIndex}].colorId">
                ${document.getElementById('colorTemplate').innerHTML}
            </select>
            <small class="text-danger"></small>
        </div>
        <div class="col-md-3">
            <label class="form-label font-weight-bold text-dark">Số Lượng</label>
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].colors[${colorIndex}].quantity" placeholder="Nhập số lượng" required>
            <small class="text-danger"></small>
        </div>
        <div class="col-md-3">
            <label class="form-label font-weight-bold text-dark">Ảnh</label>
            <input type="file" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].colors[${colorIndex}].images" required accept=".jpg, .jpeg, .png, .svg" multiple>
        </div>
        <button type="button" class="btn btn-danger btn-sm" onclick="removeColor(${variantIndex}, ${detailIndex}, ${colorIndex})">Xoá Color</button>
    `;
    document.getElementById(`colorContainer${variantIndex}_${detailIndex}`).appendChild(colorContainer);
}

function removeVariant(variantIndex) {
    const variantElement = document.querySelector(`.variant-container:nth-child(${variantIndex + 1})`);
    if (variantElement) {
        variantElement.remove();
        updateVariantIndices();
    }
}

function removeDetail(variantIndex, detailIndex) {
    const detailElement = document.querySelector(`#detailsContainer${variantIndex} .detail-container:nth-child(${detailIndex + 1})`);
    if (detailElement) {
        detailElement.remove();
        updateDetailIndices(variantIndex);
    }
}

function removeColor(variantIndex, detailIndex, colorIndex) {
    const colorElement = document.querySelector(`#colorContainer${variantIndex}_${detailIndex} .color-container:nth-child(${colorIndex + 1})`);
    if (colorElement) {
        colorElement.remove();
    }
}

function updateVariantIndices() {
    const variants = document.querySelectorAll('.variant-container');
    variants.forEach((variant, index) => {
        const title = variant.querySelector('h5');
        if (title) title.textContent = `Variant ${index + 1}`;
    });
}

function updateDetailIndices(variantIndex) {
    const details = document.querySelectorAll(`#detailsContainer${variantIndex} .detail-container`);
    details.forEach((detail, index) => {
        const title = detail.querySelector('h6');
        if (title) title.textContent = `Detail ${index + 1}`;
    });
}

document.getElementById('addProductForm').addEventListener('submit', function (e) {
    e.preventDefault();
    let isValid = true;
    console.log('submit');
    const productName = document.getElementById('name');
    if (!validateField(productName, 'Tên sản phẩm không được để trống và phải từ 5 đến 24 ký tự.', 5, 24)) {
        isValid = false;
    }

    // Kiểm tra từng variant
    document.querySelectorAll('.variant-container').forEach((variant, variantIndex) => {
        variant.querySelectorAll('.detail-container').forEach((detail, detailIndex) => {
            const colors = detail.querySelectorAll(`#colorContainer${variantIndex}_${detailIndex} .color-container`);
            if (colors.length === 0) {
                showError(detail, `Detail ${detailIndex + 1} của Variant ${variantIndex + 1} cần ít nhất một Color.`);
                isValid = false;
            } else {
                clearError(detail);
            }
            const sale = detail.querySelector(`input[name="variants[${variantIndex}].details[${detailIndex}].sale"]`);
            if (!validateSale(sale, `Giảm giá cho Detail ${detailIndex + 1} của Variant ${detailIndex + 1} phải từ 0.00 đến 1.00.`)) {
                isValid = false;
            }
        });
    });

    if (isValid) {
        this.submit();
    }
});

function showError(element, message) {
    // Lấy parent của input (thường là .form-group)
    const parent = element.closest('.form-group');
    if (!parent) {
        console.error("Không tìm thấy parent chứa lỗi cho phần tử:", element);
        return;
    }

    // Kiểm tra xem đã có phần tử .text-danger trong parent chưa
    let errorElement = parent.querySelector('.text-danger');
    if (!errorElement) {
        // Nếu chưa có, tạo mới
        errorElement = document.createElement('small');
        errorElement.className = 'text-danger';
        parent.appendChild(errorElement);
    }

    // Đặt nội dung thông báo lỗi
    errorElement.textContent = message;
}

function validateSale(input, errorMessage) {
    const value = parseFloat(input.value);
    console.log(value);
    if (isNaN(value) || value < 0.00 || value > 1.00) {
        showError(input, errorMessage);
        return false;
    }
    clearError(input);
    return true;
}

function clearError(element) {
    const error = element.querySelector('.text-danger');
    if (error) {
        error.textContent = '';
    }
}
function validateField(input, errorMessage, minLength = 1, maxLength = Infinity) {
    console.log(input);
    if (!input || input.value.trim().length < minLength || input.value.trim().length > maxLength) {
        showError(input, errorMessage);
        return false;
    }
    clearError(input);
    return true;
}