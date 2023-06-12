var app = new Vue({
    el:"#app",
    data:{
        clientInfo: {},
        email:"",
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            axios.get("/api/clients/current")
            .then((response) => {
                //get client ifo
                this.clientInfo = response.data;
            })
            .catch((error)=>{
                // handle error
                this.errorMsg = "Error al obtener datos";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        signOut: function(){
            axios.post('/api/logout')
            .then(response => window.location.href="/web/index.html")
            .catch(() =>{
                this.errorMsg = "Salir fallido"
                this.errorToats.show();
            })
        },
        create: function(){
            axios.post('/api/clients/current/accounts')
            .then(response => window.location.reload())
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },
        //actualizar email usuario http://localhost:8080
        update: function(){
                    axios.put('/api/update/email',`email=${this.email}`)
                    .then(response => window.location.reload())
                    .catch((error) =>{
                        this.errorMsg = error.response.data;
                        this.errorToats.show();
                    })
                },

    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'), {
            delay: 5000 // Duración de 5 segundos
        });
        this.getData();
    }
})