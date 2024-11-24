let variantCount = 0;

function addVariant() {
    const variantIndex = variantCount++;
    const variantContainer = document.createElement('div');
    variantContainer.className = 'variant-container p-3 mb-3 shadow-sm';
    variantContainer.innerHTML = `
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
    `;
    document.getElementById('variantsContainer').appendChild(variantContainer);
}

function addVariantDetail(variantIndex) {
    const detailContainer = document.createElement('div');
    const detailIndex = document.querySelectorAll(`#detailsContainer${variantIndex} .detail-container`).length;
    detailContainer.className = 'detail-container p-2 mb-2 shadow-sm';
    detailContainer.innerHTML = `
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
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].sale" placeholder="Nhập giảm giá" required>
            <small class="text-danger"></small>
        </div>

        <!-- Color Section -->
        <div id="colorContainer${variantIndex}_${detailIndex}" class="mt-3">
            <h6 class="font-weight-bold text-dark">Colors</h6>
        </div>
        <button type="button" class="btn btn-outline-info btn-sm" onclick="addColor(${variantIndex}, ${detailIndex})">+ Thêm Color</button>
    `;
    document.getElementById(`detailsContainer${variantIndex}`).appendChild(detailContainer);
}

function addColor(variantIndex, detailIndex) {
    const colorContainer = document.createElement('div');
    const colorIndex = document.querySelectorAll(`#colorContainer${variantIndex}_${detailIndex} .color-container`).length;
    colorContainer.className = 'color-container row mb-2';
    colorContainer.innerHTML = `
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
    `;
    document.getElementById(`colorContainer${variantIndex}_${detailIndex}`).appendChild(colorContainer);
}

// Form Validation
document.getElementById('addProductForm').addEventListener('submit', function (e) {
    console.log('submit');
    e.preventDefault(); // Prevent default submission
    let isValid = true;

    // Kiểm tra tên sản phẩm
    const productName = document.getElementById('name');
    if (!validateField(productName, 'Tên sản phẩm không được để trống và phải từ 5 đến 24 ký tự.', 5, 24)) {
        isValid = false;
    }

    // Kiểm tra Variants
    document.querySelectorAll('.variant-container').forEach((variant, index) => {
        const variantName = variant.querySelector(`input[name="variants[${index}].name"]`);
        if (!validateField(variantName, `Tên Variant ${index + 1} không được để trống.`)) {
            isValid = false;
        }

        // Kiểm tra select "Usage Category"
        const usageCategory = variant.querySelector(`select[name="variants[${index}].usageCategoryId"]`);
        if (!validateSelect(usageCategory, `Usage Category cho Variant ${index + 1} cần được chọn.`)) {
            isValid = false;
        }

        // Kiểm tra Details
        const details = variant.querySelectorAll('.detail-container');
        details.forEach((detail, detailIndex) => {
            const sale = detail.querySelector(`input[name="variants[${index}].details[${detailIndex}].sale"]`);
            if (!validateSale(sale, `Giảm giá cho Detail ${detailIndex + 1} của Variant ${index + 1} phải từ 0.00 đến 1.00.`)) {
                isValid = false;
            }

            const memory = detail.querySelector(`select[name="variants[${index}].details[${detailIndex}].memid"]`);
            if (!validateSelect(memory, `Memory cho Detail ${detailIndex + 1} của Variant ${index + 1} cần được chọn.`)) {
                isValid = false;
            }
        });
    });

    if (isValid) {
        this.submit();
    }
});

function validateField(input, errorMessage, minLength = 1, maxLength = Infinity) {
    if (!input || input.value.trim().length < minLength || input.value.trim().length > maxLength) {
        showError(input, errorMessage);
        return false;
    }
    clearError(input);
    return true;
}

function validateSelect(select, errorMessage) {
    if (!select || select.value.trim() === "") {
        showError(select, errorMessage);
        return false;
    }
    clearError(select); // Xóa lỗi nếu hợp lệ
    return true;
}


function validateSale(input, errorMessage) {
    const value = parseFloat(input.value);
    if (isNaN(value) || value < 0.00 || value > 1.00) {
        showError(input, errorMessage);
        return false;
    }
    clearError(input);
    return true;
}

function showError(input, message) {
    const error = input.nextElementSibling;
    if (error) error.textContent = message;
    console.log(message);
}

function clearError(input) {
    const error = input.nextElementSibling;
    if (error) error.textContent = '';
}