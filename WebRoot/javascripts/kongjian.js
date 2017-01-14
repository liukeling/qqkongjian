function fb(frm) {
	var neirong = frm.shuoshuoneirong.value;
	if (null == neirong || "" == neirong) {
		alert("说说不能为空");
		return false;
	}
	return true;
}
function plshow(id) {
	$("#" + id).show();
}
function plhide(id) {
	$("#" + id).hide();
}