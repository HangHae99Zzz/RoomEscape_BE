$(function(){
    const uuidInput = document.querySelector('input#uuid');

    //uuid 랜덤으로 만들기.
    function guid() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            let r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
    //브라우저 localstorage에 저장. localstorage에 uuid가 없는 경우에만.
    if (localStorage.getItem("uuid") === null) {
        localStorage.setItem("uuid", guid());
    }
    //uuid를 이제 uuidInput창에 넣어준다.
    uuidInput.value = localStorage.getItem("uuid");
    console.log("local.uuid:" + localStorage.getItem("uuid"));
    // console.log("input.value:" + uuidInput.value);
});
//방 버튼(참가) 클릭시 실행.
function addUuidToButtonLink(button) {
    let id = 'button-link-' + button.value;
    //a태그 주소 가져오기.
    let ref = document.getElementById(id).href;
    document.getElementById(id).href = ref + '/user/' + localStorage.getItem("uuid");
    console.log("link.href:" + document.getElementById(id).href);
}
