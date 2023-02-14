<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

    <%@ include file="../layout/header.jsp" %>

        <style>
            .container {
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            h2 {
                margin-top: 2rem;
            }

            form {
                width: 50%;
                margin-top: 2rem;
                display: flex;
                flex-direction: column;
                align-items: center;
                border: 1px solid gray;
                padding: 1rem;
                border-radius: 10px;
            }

            .form-group {
                margin-bottom: 1rem;
                text-align: center;
            }

            .form-group img {
                width: 320px;
                height: 288px;

                border-radius: 50%;
                margin-bottom: 1rem;
                border: 1px solid gray;
            }

            .btn {
                margin-top: 1rem;
                width: 20%;
            }
        </style>

        <div class="container my-3">
            <h2 class="text-center">프로필 사진 변경 페이지</h2>
            <%-- <form action="/user/profileUpdate" method="post" enctype="multipart/form-data"> --%>
            <form id="profileForm" action="/user/profileUpdate" method="post" enctype="multipart/form-data">
                <!--post와 ajax 두가지 방법 해보기-->
                <div class="form-group">
                        <img id="imagePreview" src="${user.profile==null ? '/images/profile.jpg' : user.profile}" alt="Current Photo" class="img-fluid">
                </div>
                <div class="form-group">
                    <input type="file" class="form-control" id="profile" name="profile" onchange="chooseImage(this)">
                    <!--input type 이 file이면 버튼이 생긴다. 파일을 넣을때는 enctype 을 multipart/form-data가 되어야한다
                        이때 enctype의 디폴트는 x-www-form-urlencoded 타입이다. this를 넣을 시 자기자신 객체를 얘기한다 -->
                </div>
                <button type="button" class="btn btn-primary" onClick="updateImage()">아작스 사진 변경</button>
                <button type="submit" class="btn btn-primary">사진 변경</button>
            </form>
        </div>
        <script>
        //ajax
            function updateImage(){
                // console.log( $("#profileForm"));
                let profileForm = $("#profileForm")[0];
                let formData = new FormData(profileForm); //자바 스크립트로 폼 태그 정보를 다 들고온다. 
                console.log( formData);

                $.ajax({
                    type: "put",
                    url: "/user/profileUpdate",
                    data: formData,
                    contentType : false, // 필수 ,x-www-form-urlencoded로 파싱 되는 것을 방지하는 프로토콜
                    processData : false, // 필수 , contentType을 false로 줬을 때 QueryString이 자동 설정됨. 이를 해제 
                    enctype : "multipart/form-data",
                    dataType : "text"
                    //contentType이 아니라 이걸로 보냄 (put 요청이기 때문에 body 데이터 확인하고 contentType 확인하러감)
                })
                .done(res => { 
                    console.log(res);
                    alert(res.msg);
                    location.href ="/";
                }).fail((err)=>{
                    console.log(err);
                    alert(err.responseJSON.msg);
                });
            }

            function chooseImage(obj) { //변경을 인식 (onchange)
                // alert("사진이 변경됨");
                // console.log(obj);
                // console.log(obj.files);
                let f = obj.files[0];
                // let fname = f.name;
                // let ftype = f.type;
                // console.log("name : " + fname + "  ftype : " + ftype);
                if(!f.type.match("image.*")){ //mime타입을 비교해서 찾아줌 
                    alert("다시 이미지를 등록해주세요");
                    //사진이 아닌걸 넣을 수 없음 
                    return;
                }
                let reader = new FileReader();
                reader.readAsDataURL(f); //하드디스크에 접근해서 읽어오기 때문에 IO임. (오래걸림!)
                //하드디스크는 상처로 기록, 램은 전류로 데이터를 기록 (캐싱 데이터)
                //왜 void 타입일까??? -> 콜백함수로 응답을 준다는 의미 
                //싱글 스레드는 파일을 받을 동안 여기서 기다리는 동안 화면이 멈출 수 있음 (자바라면 스레드로 받았을 텐데)
                //근데 await 하려면 리턴타입이 프로미스여야함. 
                //-> 오래걸리는 연산이기 때문에 이벤트 큐 에다가 등록해준다. 등록 된 후, 바로 실행되지 않는다
                //콜 스택에 현재 필요한것부터 순차적으로 다 실행 한다. 이때, 스크립트 안에 function 자체는 콜스택에 안들어감. 이름만 들어감
                //function이 실행되면 그 function도 콜 스택에 들어가는데 reader.readAsDataURL의 경우 이벤트 큐에 들어가게된다. 
                //등록 후 함수가 빠져나가면, 콜스택에 들어간 모든 것을 시작하고, 끝나면! 이벤트 큐로 들어간다. 
                //이벤트 큐에서 파일이 다 읽어졌는지 확인 한다. -> 계속 확인함. 
                // 다 되었으면 콜백함수로 알려준다. 


                //콜백? : 이벤트가 끝나면 어떤 행위를 하고 싶은가. 그래서 함수로 표현한다.
                //onload는 콜 스택이 다 비워지고, 이벤트 루프로 가서 readAsDataURL 이벤트가 끝나면
                //콜백 시켜주는 함수 
                reader.onload = function (e) {
                    // console.log(e);
                    $("#imagePreview").attr("src", e.target.result);
                }
            }
        </script>
        <%@ include file="../layout/footer.jsp" %>