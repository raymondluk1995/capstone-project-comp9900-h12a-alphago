<template>
    <div class="footer">
    <el-row type="flex" justify="center" style="align-items: center;">
      <div class="footer-wrap">
        <p>
          wwww.onlinepropertysales.com
        </p>
        <p>
          Copyright &copy; 2020. All rights reserved(v1.0.0)
        </p>
      </div>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      logo:
        ''
    };
  },
};
</script>

<style scoped lang="scss">
.footer {
  width: 100%;
  // margin-top: 5px;
  // padding: 5px;
  background-color: #1e4061;
  color: #fff;
  position: fixed;
  bottom: 0;
  height: 5%;
  font-size:10px;

  .footer-wrap {
    text-align: center;
    p {
      margin: 5px 0;
    }
  }
}

</style>
