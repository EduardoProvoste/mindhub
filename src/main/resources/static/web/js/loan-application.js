var app = new Vue({
    el:"#app",
    data:{
        loanTypes: [],
        loanTypeId: 0,
        payments: 0,
        paymentsList: [],
        clientAccounts: [],
        errorToats: null,
        errorMsg: null,
        accountToNumber: "VIN",
        amount: 0,
        loanApprovalMessage: '',
        fees: []
    },
    methods:{
        getData: function(){
            Promise.all([axios.get("/api/loans"),axios.get("/api/clients/current/accounts")])
            .then((response) => {
                //get loan types ifo
                this.loanTypes = response[0].data;
                this.clientAccounts = response[1].data;
            })
            .catch((error) => {
                this.errorMsg = "Error al obtener datos";
                this.errorToats.show();
            })
        },
        formatDate: function(date){
            return new Date(date).toLocaleDateString('en-gb');
        },
        checkApplication: function(){
            if(this.loanTypeId == 0){
                this.errorMsg = "Debe seleccionar un tipo de préstamo";
                this.errorToats.show();
            }
            else if(this.payments == 0){
                this.errorMsg = "Debe seleccionar pagos";
                this.errorToats.show();
            }
            else if(this.accountToNumber == "VIN"){
                this.errorMsg = "Debe indicar una cuenta";
                this.errorToats.show();
            }
            else if(this.amount == 0){
                this.errorMsg = "Debes indicar una cantidad";
                this.errorToats.show();
            }else{
                this.modal.show();
            }
        },
        apply: function(){
            axios.post("/api/loans",{loanId: this.loanTypeId, amount: this.amount, payments: this.payments, toAccountNumber: this.accountToNumber})
            .then(response => { 
                this.modal.hide();
                this.okmodal.show();
                this.loanApprovalMessage = response.data;
            })
            .catch((error) =>{
                this.errorMsg = error.response.data;  
                this.errorToats.show();
            })
        },
        changedType: function(){
            this.paymentsList = this.loanTypes.find(loanType => loanType.id == this.loanTypeId).payments;
        },
        finish: function(){
            window.location.reload();
        },
        checkFees: function(){
            this.fees = [];
            this.totalLoan = parseInt(this.amount) + (this.amount * 0.2);
            let amount = this.totalLoan / this.payments;
            for(let i = 1; i <= this.payments; i++){
                this.fees.push({ amount: amount });
            }
            this.feesmodal.show();
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
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.modal = new bootstrap.Modal(document.getElementById('confirModal'));
        this.okmodal = new bootstrap.Modal(document.getElementById('okModal'));
        this.feesmodal = new bootstrap.Modal(document.getElementById('feesModal'));
        this.getData();
    }
})