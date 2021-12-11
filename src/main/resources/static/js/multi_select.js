jQuery.fn.listtosel = function () {
   var $obj = $(this);
   var $list = $obj.find(".schlist");
   var $sellst = $obj.find(".sellist");

   var $ctrl = $obj.find(".ctrl");
   var $allsel = $ctrl.find(".allsel");
   var $onesel = $ctrl.find(".onesel");
   var $onedesel = $ctrl.find(".onedesel");
   var $alldesel = $ctrl.find(".alldesel");

   var dom, idx;

   // 전체 선택
   $allsel.on("click", function () {
      if ($list.find("option").length != 0) {
         dom = $list.find("select").html();
         $sellst.find("select").append(dom);
         $list.find("option").remove();
      }

      return false;
   });

   // 전체 취소
   $alldesel.on("click", function () {
      if ($sellst.find("option").length != 0) {
         dom = $sellst.find("select").html();
         $list.find("select").append(dom);
         $sellst.find("option").remove();
      }

      return false;
   });

   // 하나 선택
   $onesel.on("click", function () {
      $list.find("option").each(function () {
         if ($(this).is(":selected") == true) {
            dom = this;
            $sellst.find("select").append(dom);
         }
      });

      return false;
   });

   // 하나 취소
   $onedesel.on("click", function () {
      $sellst.find("option").each(function () {
         if ($(this).is(":selected") == true) {
            dom = this;
            $list.find("select").append(dom);
         }
      });

      return false;
   });
}

$(function () {
   $(".listtosel").listtosel();
});