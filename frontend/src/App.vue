<template>
  <div id="app" class="wrapper">
    <!--    <Header>-->
    <!--      <el-button round type="primary" @click="back">Back</el-button>-->
    <!--    </Header>-->
    <div class="main-content">
      <router-view />
    </div>
    
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

  .wrapper{
    position: relative;  
    height: auto;   
    min-height: 100%;
  }

  .main-content{  
   padding-bottom: 60px;  
  }  

  html{
    height:100%;
  }

  body{
    height:100%;
  }

  Footer{
   position: absolute;  
   bottom: 0; /* 关键 */  
   left:0; /* IE下一定要记得 */  
  }
</style>
