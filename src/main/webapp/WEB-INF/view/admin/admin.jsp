<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../layout/header.jsp" %>
        <div class="container mt-3">
            <h2>관리자 페이지</h2>

            <form class="d-flex my-3">
                <input id="serchText" class="form-control me-2" type="text" placeholder="Search">
                <button class="btn btn-primary" type="button" onClick="search(this)">Search</button>
            </form>

            <div class="row">
                <div class="col-2 list-group">
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="user" class="list-group-item list-group-item-action"> 회원 관리 </a>
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="board" class="list-group-item list-group-item-action"> 게시글 관리 </a>
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="reply" class="list-group-item list-group-item-action"> 댓글 관리 </a>
                </div>
                <div class="col" >
                    <table class="table">
                        <thead id="theadSet">
                            
                        </thead>
                        <tbody id="tbodySet">
                           
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

<div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel"> </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form>
          <div class="mb-3">
            <label for="recipient-name" class="col-form-label">title:</label>
            <input type="text" class="form-control" id="toTitle"/>
          </div>
          <div class="mb-3">
            <label for="message-text" class="col-form-label">Message:</label>
            <textarea class="form-control" id="toMessage"></textarea>
          </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="modalBtn"  >Send message</button>
      </div>
    </div>
  </div>
</div>

        <script>
        let state = [];

        function search(){
            let filterTest = $("#serchText").val();            
            if(state[0] == undefined) {
                alert("먼저 관리를 클릭해 주세요");
                return ;
            }
            if(filterTest == undefined || filterTest==("")) {
                alert("검색어를 적어주세요");
                return ;
            }
            let e =  state[0];
            // console.log(e);
            $("#theadSet").empty();
            $("#tbodySet").empty();
            if(e.email!=null){
                // console.log(e.email);
                let newState = state.filter((data) => data.username.includes(filterTest));
                render(newState, 'user');
            }else if(e.title!=null){
                let newState = state.filter((data) => data.title.includes(filterTest));
                render(newState, 'board');
            }else if(e.comment!=null){
                let newState = state.filter((data) => data.comment.includes(filterTest));
                render(newState, 'reply');
            }
        }

        function callFunction(e){
            // console.log(e.id);
                $.ajax({
                    type: "get",
                    url: "/admin/"+e.id,
                    dataType: "json"
                })
                .done(res => { 
                    // console.log(res);
                    if (res.code == 1) {
                        $("#theadSet").empty();
                        $("#tbodySet").empty();
                        state = res.data;
                        render(state , e.id);
                    }
                }).fail((err)=>{
                    console.log(err);
                    alert(err.responseJSON.msg);
                });
        };

        function render(datas , table) {
            dKeys = Object.keys(datas[0]);
            let elh = `<tr>`;
            // console.log(dKeys);
            for(key of dKeys){
                // console.log(key);
                elh +=`<td>`+key+`</td>`;
            }
            if(table=='user'){
                 elh += `<td>eamil보내기</td>`;
            }
            elh += `<td>삭제</td></tr>`;
            $("#theadSet").prepend(elh);
           

            datas.forEach((data) => {
                // console.log(data);
                //onclick="location.href='/`+table+`/`+data.id+`'"
                let elt = `<tr id="data-`+data.id+`" >`;
                for(key in data){
                    elt +=`<td>`+data[key]+`</td>`;
                }
                
                if(table=="user"){
                    // console.log(table);
                    elt += `<td><button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#exampleModal" data-bs-whatever="@getbootstrap"
                            value ="`+data.email+`">전송</button></td>`;
                    //elt += `<td> <button onClick="emailShot('`+data.email+`')"> 전송 </button> </td>`;
                    //$("#exampleModal").attr("value",data.email);
                    //onClick="emailShot()"
                    $("#modalBtn").attr("onClick","emailShot('"+data.email+"')");
                }
                elt += `<td> <button onClick="deleteById(`+data.id+`,`+table+`)"> 삭제 </button> </td></tr>`;
               $("#tbodySet").prepend(elt);
            });
        }

        function emailShot(email){
            let data = {
                "email" : email,
                "title" : $("#toTitle").val(),
                "message" : $("#toMessage").val()
            };
            // console.log(data);
            $.ajax({
                url: '/admin/mail',
                data: JSON.stringify(data),
                type: 'post',
                contentType : "application/json; charset=utf-8",
                dataType: "json"
                })
                .done(res => {
                    // var myModal = $("#exampleModal").modal();
                    $("#toTitle").val("");
                    $("#toMessage").val("");
                    $('#exampleModal').modal("toggle");
                    // myModal.toggle();
                    console.log(res);
                    alert(res.msg);
                    // parsing = JSON.parse(data.data);
                })
                .fail(err => { 
                    alert(err.msg);
                    // console.log(err);
                })
            };
        

        function deleteById(id, table) {
                // console.log(table.id);
                $.ajax({
                    type: "delete",
                    url: "/"+table.id+"/" + id,
                    dataType: "json"
                })
                .done(res => { //20X 일때
                    $(`#data-`+id).remove();  
                    alert(res.msg);
                })
                .fail(err => { //40X , 50X 일때
                    alert(err.responseJSON.msg);
                });
        }
        </script>
        <%@ include file="../layout/footer.jsp" %>