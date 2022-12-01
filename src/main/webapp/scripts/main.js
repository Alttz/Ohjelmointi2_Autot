//funktio lomaketietojen muuttamiseksi JSON-stringiksi
function serialize_form(form){
	return JSON.stringify(
	    Array.from(new FormData(form).entries())
	        .reduce((m, [ key, value ]) => Object.assign(m, { [key]: value }), {})
	        );	
} 

//funktio arvon lukemiseen urlista avaimen perusteella
function requestURLParam(sParam) {
	let sPageURL = window.location.search.substring(1);
	let sURLVariables = sPageURL.split("&");
	for (let i = 0; i < sURLVariables.length; i++) {
		let sParameterName = sURLVariables[i].split("=");
		if(sParameterName[0] == sParam) {
			return sParameterName[1];
		}
	}
}

//Tutkitaan lisättävät tiedot ennen niiden lähettämistä backendiin
function tutkiJaLisaa(){
	if(tutkiTiedot()) {
		lisaaTiedot();
	}
}

//Tutkitaan päivitettävät tiedot ennen niiden lähettämistä backendiin
function tutkiJaPaivita(){
	if(tutkiTiedot()) {
		paivitaTiedot();
	}
}

//funktio syöttötietojen tarkistamista varten (yksinkertainen)
function tutkiTiedot(){
	let ilmo="";
	let d = new Date();
	if(document.getElementById("rekno").value.length<3) {
		ilmo="Rekisterinumero ei kelpaa!";
		document.getElementById("rekno").focus();
	} else if (document.getElementById("merkki").value.length<2) {
		ilmo="Merkki ei kelpaa!";
		document.getElementById("merkki").focus();
	} else if (document.getElementById("malli").value.length<1) {
		ilmo="Malli ei kelpaa!";
		document.getElementById("malli").focus();
	} else if (document.getElementById("vuosi").value*1!=document.getElementById("vuosi").value) {
		ilmo="Vuosi ei ole luku!";
		document.getElementById("vuosi").focus();
	} else if(document.getElementById("vuosi").value<1900 || document.getElementById("vuosi").value>d.getFullYear()+1){
		ilmo="Vuosi ei kelpaa!";	
		document.getElementById("vuosi").focus();	
	}
	if(ilmo!=""){
		document.getElementById("ilmo").innerHTML=ilmo;
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 3000);
		return false;
	}else{
		document.getElementById("rekno").value=siivoa(document.getElementById("rekno").value);
		document.getElementById("merkki").value=siivoa(document.getElementById("merkki").value);
		document.getElementById("malli").value=siivoa(document.getElementById("malli").value);
		document.getElementById("vuosi").value=siivoa(document.getElementById("vuosi").value);	
		return true;
	}
}

//Funktio XSS-hyökkäysten estämiseksi (Cross-site scripting)
function siivoa(teksti){
	teksti=teksti.replace(/</g, "");//&lt;
	teksti=teksti.replace(/>/g, "");//&gt;	
	teksti=teksti.replace(/'/g, "''");//&apos;	
	return teksti;
}

function varmistaPoisto(id, rekno) {
	if (confirm("Poista auto " + decodeURI(rekno) +"?")) { //decodeURI() muutetaan enkoodatut merkit takaisin normaaliksi kirjoitukseksi
		poistaAuto(id, encodeURI(rekno));
	}
}

function asetaFocus(target) {
	document.getElementById(target).focus();
}

//Funktio Enter-nappiin. Kutsu bodyn onkeydown()-metodista.
function tutkiKey(event, target) {
	console.log(event.keyCode);
	if(event.keyCode==13) { //13 = enter
		if(target=="listaa") {
			haeAutot();
		} else if (target=="lisaa") {
			tutkiJaLisaa();
		} else if (target=="paivita") {
			tutkiJaPaivita();
		}
	} else if(event.keyCode==113) {//F2
		document.location="listaaautot.jsp";
	}
}
