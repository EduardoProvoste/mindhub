var app = new Vue({
    el:"#app",
    data:{
        accountInfo: {},
        errorToats: null,
        errorMsg: null,
    },
    methods:{
        getData: function(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            axios.get(`/api/accounts/${id}`)
            .then((response) => {
                //get client ifo
                this.accountInfo = response.data;
                this.accountInfo.transactions.sort((a,b) => parseInt(b.id - a.id))
            })
            .catch((error) => {
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
    },
    mounted: function(){
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'), {
            delay: 5000 // Duración de 5 segundos
            });
        this.getData();
    }
})