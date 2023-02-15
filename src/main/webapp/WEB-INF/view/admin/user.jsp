<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ include file="../layout/header.jsp" %>
        <div class="container mt-3">
            <h2>관리자 페이지</h2>

            <div class="row">
                <div class="col-sm-2 list-group">
                    <a href="/admin/user"  class="list-group-item list-group-item-action">회원 관리</a>
                    <a href="/admin/board"  class="list-group-item list-group-item-action">게시글 관리</a>
                    <a href="/admin/reply"  class="list-group-item list-group-item-action">댓글 관리</a>
                </div>
                <div class="col" id="dataBox">
                    <table class="table">
                        <thead>
                             <tr> <td>id</td> <td>username</td> <td>password</td> <td>email</td> <td>createdAt</td> <td>profile</td> <td>role</td> <td></td> </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${datas}" var="data">
                            <tr id="data-${data.id}"> <td>${data.id}</td> <td>${data.username}</td> <td>${data.password}</td> 
                            <td>${data.email}</td> <td>${data.createdAt}</td> <td>${data.profile}</td> <td>${data.role}</td>
                            <td><button onclick="deleteById(${data.id})"> 삭제 </button></td></tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script>
        
        
        function deleteById(id) {
                $.ajax({
                    type: "delete",
                    url: "/user/" + id,
                    dataType: "json"
                })
                .done(res => { //20X 일때
                    $(`#data-`+id).remove();  
                    alert(res.msg);
                    location.reload();
                })
                .fail(err => { //40X , 50X 일때
                    alert(err.responseJSON.msg);
                });
        }

        </script>
        <%@ include file="../layout/footer.jsp" %>