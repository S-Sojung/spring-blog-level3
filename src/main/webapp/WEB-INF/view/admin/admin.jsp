<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../layout/header.jsp" %>
        <div class="container mt-3">
            <h2>관리자 페이지</h2>

            <div class="row">
                <div class="col-sm-2 list-group">
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="user" class="list-group-item list-group-item-action">회원 관리</a>
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="board" class="list-group-item list-group-item-action">게시글 관리</a>
                    <a href="javascript:void(0)" onclick="callFunction(this)" id="reply" class="list-group-item list-group-item-action">댓글 관리</a>
                </div>
                <div class="col" id="dataBox">
                    <table class="table">
                        <thead id="theadSet">
                            
                        </thead>
                        <tbody id="tbodySet">
                           
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script>
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
                        render(res.data , e.id);
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
            elh += `<td></td></tr>`;
            $("#theadSet").prepend(elh);
           

            datas.forEach((data) => {
                // console.log(data);
                //onclick="location.href='/`+table+`/`+data.id+`'"
                let elt = `<tr id="data-`+data.id+`" >`;
                for(key in data){
                    elt +=`<td>`+data[key]+`</td>`;
                }
                elt += `<td> <button onclick="deleteById(`+data.id+`,`+table+`)"> 삭제 </button> </td></tr>`;
               $("#tbodySet").prepend(elt);
            });
        }

        function deleteById(id, table) {
                console.log(table.id);
                $.ajax({
                    type: "delete",
                    url: "/"+table.id+"/" + id,
                    dataType: "json"
                })
                .done(res => { //20X 일때
                    $(`#data-`+id).remove();  
                    alert(res.msg);
                    $("#dataBox").load(location.href+' #dataBox');
                })
                .fail(err => { //40X , 50X 일때
                    alert(err.responseJSON.msg);
                });
        }
        </script>
        <%@ include file="../layout/footer.jsp" %>