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
        </div>

        <!-- Usage Category Combobox -->
        <div class="form-group">
            <label for="variants${variantIndex}.usageCategoryId" class="form-label font-weight-bold text-dark">Usage Category</label>
            <select class="form-control" name="variants[${variantIndex}].usageCategoryId">
                ${document.getElementById('usageCategoryTemplate').innerHTML}
            </select>
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
        </div>

        <!-- Giá -->
        <div class="form-group">
            <label class="form-label font-weight-bold text-dark">Giá</label>
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].price" placeholder="Nhập giá" required>
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
        </div>
        <div class="col-md-3">
            <label class="form-label font-weight-bold text-dark">Số Lượng</label>
            <input type="number" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].colors[${colorIndex}].quantity" placeholder="Nhập số lượng" required>
        </div>
        <div class="col-md-3">
            <label class="form-label font-weight-bold text-dark">Ảnh</label>
            <input type="file" class="form-control" name="variants[${variantIndex}].details[${detailIndex}].colors[${colorIndex}].image" required>
        </div>
    `;
    document.getElementById(`colorContainer${variantIndex}_${detailIndex}`).appendChild(colorContainer);
}
