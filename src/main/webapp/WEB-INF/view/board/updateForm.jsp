<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file ="../layout/header.jsp"%>
    <div class="container my-3">
        <form>
            <div class="form-group mb-2">
                <input type="text" class="form-control" placeholder="Enter title" name="title" id="title" value="${board.title}">
            </div>

            <div class="form-group mb-2">
                <textarea class="form-control summernote" rows="5" id="content" name="content">${board.content}</textarea>
            </div>
        <button type="button" class="btn btn-primary" onClick="updateById(${board.id})">글수정완료</button>
        </form>
    </div>

    <script>
        function updateById(id) {
            let boardUpdateReqDto={
                "title": $("#title").val(),
                "content": $("#content").val()
            };
            // let putUrl = "/board/"+id+"?title="+boardUpdateReqDto.title+"&content="+boardUpdateReqDto.content;
            $.ajax({
                type: "put",
                url: "/board/"+id,
                data: JSON.stringify(boardUpdateReqDto),
                headers: {
                    "Content-Type": "application/json; charset=utf-8"
                    //인증이 필요하면 여기에 쿠키나 인증이 들어가야함
                    },
                dataType: "json" // default : 응답의 mime타입으로 유추함 
                // accepts : "application/json" //기대함
                })
                .done(res => { //20X 일때
                    // res.data = boardUpdateReqDto;
                    alert(res.msg);
                    location.href ="/board/" + id;
                })
                .fail(err => { //40X , 50X 일때\
                    console.log(err.responseJSON.msg);
                    alert(err.responseJSON.msg);
                });
        }
    </script>

    <script>
        $('.summernote').summernote({
            tabsize: 2,
            height: 400
        });
    </script>
<%@ include file ="../layout/footer.jsp"%>