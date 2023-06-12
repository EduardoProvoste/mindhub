var app = new Vue({
  el: "#app",
  data: {
    clientInfo: {},
      clientAccounts: [],
      responseMessage: "",
      valorUf: 0,
      valorDollar: 0,
    errorToats: null,
    errorMsg: null,
  },
  methods: {
    getData: function () {
      Promise.all([
        axios.get("/api/clients/current"), axios.get("/api/clients/current/accounts"), axios.get("/chat/message"), axios.get("/chat/uf"), axios.get("/chat/dollar")])
        .then((response) => {
          //get client ifo
          this.clientInfo = response[0].data;
            this.clientAccounts = response[1].data;
            this.responseMessage = response[2].data;
            this.valorUf = response[3].data;
            this.valorDollar = response[4].data;
        })
        .catch((error) => {
          // handle error
          this.errorMsg = "Error al obtener datos";
          this.errorToats.show();
        });
    },
    formatDate: function (date) {
      return new Date(date).toLocaleDateString("en-gb");
    },
    signOut: function () {
      axios
        .post("/api/logout")
        .then((response) => (window.location.href = "/web/index.html"))
        .catch(() => {
          this.errorMsg = "Salir fallido";
          this.errorToats.show();
        });
    },
  },
  mounted: function () {
    this.errorToats = new bootstrap.Toast(
      document.getElementById("danger-toast")
    );
    this.getData();
  },
});
