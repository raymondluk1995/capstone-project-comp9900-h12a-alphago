<template>
  <div class="header">
    <el-row type="flex" justify="space-around" style="align-items: center">
<!--    <el-row :gutter="20">-->
    <el-col :span="8">
        <img @click="goto('home')" src="https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1898022387,1431488990&fm=26&gp=0.jpg"
             alt="render failure" style="width: 120px; height:90px; cursor: pointer">
      </el-col>
      <el-col :span="8"  style="align-items: center">
        <div class="slogan" >
          <h4 style="font-family:Georgia;color: #222222">THIS iS A TITLE</h4>
          <!--
          <img @click="linkToBack" src="@/assets/slogan.png" alt=""
               style="width: 180px; height: 70px; cursor: pointer">
                -->
        </div>
      </el-col>

<!--      <el-row type="flex" justify="center" style="align-items: center;">-->
        <el-col :span="4">
          <slot></slot>
        </el-col>
<!--      </el-row>-->
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
    };
  },
  methods: {
    goto(name) {
      console.log(name);
      this.$router.push({ name: name });
    },
  },
};
</script>

<style scoped lang="scss">
.header {
  /*margin-bottom: 30px;*/
  margin-left: 50px;
  margin-right: 50px;
  padding: 15px;
  border-bottom: 2px solid #f2f2f2;
}
</style>
