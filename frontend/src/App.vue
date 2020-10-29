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
      this.Height = document.documentElement.clientHeight - 44;
      //监听浏览器窗口变化　
      // window.onresize = ()=> {this.Height = document.documentElement.clientHeight -100}
      window.onresize = ()=> {this.Height = document.documentElement.clientHeight -44};
    },
    data() {
      return {
        Height: 0,
        title: document.title,
      };    
    },
    watch:{
      $route:{
        handler: function(){
          var path = this.$route.path;
          if(path!="/alpha"){
            var canvas = document.getElementById("c_n3");
            canvas.style.display = "none";
            // console.log("canvas concealed");
          }
          else{
            var canvas = document.getElementById("c_n3");
            canvas.style.display = "block";
          }
        }
      }
    }
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
