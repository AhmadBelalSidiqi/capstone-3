let cartService;

class ShoppingCartService {

    cart = {
        items:[],
        total:0
    };

    addToCart(productId)
    {
        const url = `${config.baseUrl}/cart/products/${productId}`;
        // const headers = userService.getHeaders();

        axios.post(url, {})// ,{headers})
            .then(response => {
                this.setCart(response.data)

                this.updateCartDisplay()

            })
            .catch(error => {

                const data = {
                    error: "Add to cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    setCart(data)
    {
        this.cart = {
            items: [],
            total: 0
        }

        this.cart.total = data.total;

        for (const [key, value] of Object.entries(data.items)) {
            this.cart.items.push(value);
        }
    }

    loadCart(callback)
    {
        const url = `${config.baseUrl}/cart`;

        axios.get(url)
            .then(response => {
                this.setCart(response.data)
                this.updateCartDisplay()
                if(callback) callback();
            })
            .catch(error => {
                const data = {
                    error: "Load cart failed."
                };

                templateBuilder.append("error", data, "errors")
            })
    }

    loadCartPage()
    {
        if(userService.isLoggedIn()) {
            this.loadCart(() => this.renderCartPage());
        }
        else {
            this.renderCartPage();
        }
    }

    renderCartPage()
    {
        const main = document.getElementById("main")
        main.innerHTML = "";

        let div = document.createElement("div");
        div.classList="filter-box";
        main.appendChild(div);

        const contentDiv = document.createElement("div")
        contentDiv.id = "content";
        contentDiv.classList.add("content-form");

        const cartHeader = document.createElement("div")
        cartHeader.classList.add("cart-header")

        const h1 = document.createElement("h1")
        h1.innerText = "Cart";
        cartHeader.appendChild(h1);

        const actions = document.createElement("div")
        actions.classList.add("cart-actions")

        const clearButton = document.createElement("button");
        clearButton.classList.add("btn", "btn-outline-danger")
        clearButton.innerText = "Clear";
        clearButton.addEventListener("click", () => this.clearCart());
        actions.appendChild(clearButton)

        const checkoutButton = document.createElement("button");
        checkoutButton.classList.add("btn", "btn-success", "ms-2")
        checkoutButton.innerText = "Checkout";
        checkoutButton.disabled = this.cart.items.length === 0 || !userService.isLoggedIn();
        checkoutButton.addEventListener("click", () => this.checkout());
        actions.appendChild(checkoutButton)

        cartHeader.appendChild(actions)
        contentDiv.appendChild(cartHeader)

        if(this.cart.items.length === 0)
        {
            const empty = document.createElement("div")
            empty.classList.add("empty-cart")
            empty.innerText = userService.isLoggedIn() ? "Your cart is empty." : "Log in to add products to your cart."
            contentDiv.appendChild(empty)
        }
        else
        {
            this.cart.items.forEach(item => {
                this.buildItem(item, contentDiv)
            });

            const totalDiv = document.createElement("div");
            totalDiv.classList.add("cart-total");
            totalDiv.innerText = `Order Total: $${this.cart.total.toFixed(2)}`;
            contentDiv.appendChild(totalDiv);
        }

        main.appendChild(contentDiv);
    }

    checkout()
    {
        if(!userService.isLoggedIn())
        {
            const data = { error: "Please log in before checking out." };
            templateBuilder.append("error", data, "errors");
            return;
        }

        const url = `${config.baseUrl}/orders`;
        axios.post(url)
            .then(() => {
                this.cart = { items: [], total: 0 };
                this.updateCartDisplay();
                const data = { message: "Checkout complete! Your order has been placed." };
                templateBuilder.append("message", data, "errors");
                this.loadCartPage();
            })
            .catch(error => {
                const data = { error: "Checkout failed. Please try again." };
                templateBuilder.append("error", data, "errors");
            });
    }

    buildItem(item, parent)
    {
        let outerDiv = document.createElement("div");
        outerDiv.classList.add("cart-item");

        let div = document.createElement("div");
        outerDiv.appendChild(div);
        let h4 = document.createElement("h4")
        h4.innerText = item.product.name;
        div.appendChild(h4);

        let photoDiv = document.createElement("div");
        photoDiv.classList.add("photo")
        let img = document.createElement("img");
        img.src = `images/products/${item.product.imageUrl}`
        img.addEventListener("click", () => {
            showImageDetailForm(item.product.name, img.src)
        })
        photoDiv.appendChild(img)
        let priceH4 = document.createElement("h4");
        priceH4.classList.add("price");
        priceH4.innerText = `$${item.product.price}`;
        photoDiv.appendChild(priceH4);
        outerDiv.appendChild(photoDiv);

        let descriptionDiv = document.createElement("div");
        descriptionDiv.innerText = item.product.description;
        outerDiv.appendChild(descriptionDiv);

        let quantityDiv = document.createElement("div")
        quantityDiv.innerText = `Quantity: ${item.quantity}`;
        outerDiv.appendChild(quantityDiv)


        parent.appendChild(outerDiv);
    }

    clearCart()
    {

        const url = `${config.baseUrl}/cart`;

        axios.delete(url)
             .then(response => {
                 this.cart = {
                     items: [],
                     total: 0
                 }

                 this.cart.total = response.data.total;

                 for (const [key, value] of Object.entries(response.data.items)) {
                     this.cart.items.push(value);
                 }

                 this.updateCartDisplay()
                 this.loadCartPage()

             })
             .catch(error => {

                 const data = {
                     error: "Empty cart failed."
                 };

                 templateBuilder.append("error", data, "errors")
             })
    }

    updateCartDisplay()
    {
        try {
            const itemCount = this.cart.items.length;
            const cartControl = document.getElementById("cart-items")

            cartControl.innerText = itemCount;
        }
        catch (e) {

        }
    }
}





document.addEventListener('DOMContentLoaded', () => {
    cartService = new ShoppingCartService();

    if(userService.isLoggedIn())
    {
        cartService.loadCart();
    }

});
