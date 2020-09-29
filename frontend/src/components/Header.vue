<template>
  <div class="header">
    <el-row type="flex" justify="center">
        <el-col :span="16">
            <el-row type="flex" justify="space-between" class="header-content">
          <el-col :span="8">
            <img @click="goto('home')" src="@/assets/Alogo.png"
                 alt="render failure" style="width: 100px; height:90px; cursor: pointer">
          </el-col>
          <el-col :span="8">
            <div class="slogan" >
              <h4 style="font-family:Georgia;color: #222222">ALPHAGO AUCTION</h4>
            </div>
          </el-col>

            <el-col :span="4">
              <slot></slot>
            </el-col>
      </el-row>
        </el-col>
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
  margin-left: 15px;
  margin-right: 15px;
  padding: 15px;
  border-bottom: 2px solid #f2f2f2;
}
.header-content {
    .el-col {
        display: flex;
        align-items: center;
    }
}
</style>
