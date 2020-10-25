<template>
  <div id="app">
    <div id="v-content" v-bind:style="{minHeight: Height+'px'}"><router-view /></div>
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
    mounted(){
      //动态设置内容高度 让footer始终居底   header+footer的高度是100
      // this.Height = document.documentElement.clientHeight - 100;
      this.Height = document.documentElement.clientHeight - 45;
      //监听浏览器窗口变化　
      // window.onresize = ()=> {this.Height = document.documentElement.clientHeight -100}
      window.onresize = ()=> {this.Height = document.documentElement.clientHeight -45}
    },
    data() {
      return {
        Height: 0
      };
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
