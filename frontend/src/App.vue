<template>
  <div id="app">
    <router-view />
    <Footer></Footer>
  </div>
</template>

<script>
  import Footer from "./components/Footer.vue";
  export default {
    name: "Home",
    components: {
      Footer,
    },
    data() {
      return {};
    },
  };
</script>
<style lang="scss">
  * {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
  }
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
  }
</style>
