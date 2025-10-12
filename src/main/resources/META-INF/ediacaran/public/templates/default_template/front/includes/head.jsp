<%@taglib uri="https://www.uoutec.com.br/ediacaran/tags/components" prefix="ec"%>
<ec:resources type="css"/>
<ec:resources type="js"/>
<style type="text/css">

.btn-default {
    background-color: #e9ecef;
    border-color: #ced4da;
}

.btn-default .fa:not([class*="fa-inverse"]) {
    color: #000000;
}

header {
	background: #f53d2d;
}

.inner-headline {
	background: linear-gradient(-180deg, #f53d2d, #f63);
	min-height: 3em;
}

.inner-headline ul.breadcrumb {
    margin: 0px 0px;
}

.inner-headline.top-header {
	padding-top: 0em;
	padding-bottom: 0em;
	background: transparent;
	
	/*background: linear-gradient(-180deg, #313030, #5b5b5b);*/
}

.btn-default {
    background-color: #e9ecef;;
    border-color: #ced4da;
}

.btn-default .fa:not([class*="fa-inverse"]) {
    color: #000000;
}

.category img {
	max-height: 100px;
	aspect-ratio: 1/1;
}

.category a {
	color: #000000;
}

.category a:hover {
	color: #000000;
	text-decoration: none;
}

.product {
	border: 1px solid #f5f5f5;
	display: inline-grid;
	border-radius: 4px;
	padding: 1em 1em;
}

.product {
	border: 1px solid #f5f5f5;
	display: inline-grid;
	border-radius: 4px;
	padding: 1em 1em;
}

.product  a{
	color: #000000;
	text-decoration: none;
}

.product  a:hover {
	color: inherit;
	text-decoration: none;
}

.product .image img {
	max-height: 220px;
	aspect-ratio: 1/1;
}

.product .title {
	font-size: 16px;
    line-height: 24px;
}

.product .price {
	font-size: 28px;
}

    
.navbar-brand{
	display: none;
} 
    
#top_menu{
	padding: 0px 0px;
	/*background-color: #252525;*/
	background: transparent;
}

#top_menu .nav-link {
    text-transform: capitalize;
}

#top_menu a{
	color: #FFFFFF;
}

.icondefault.fa:not([class*="fa-inverse"]) {
	color: #ffffff;
}

.line-cart{
	padding-top: 1em;
}

.form {
  height: 50px;
  display: flex;
  align-items: center;
}

section.body{
	padding-top: 1em;
}

</style>
<script type="text/javascript">
	messages.setDefaultLanguage('pt-BR');
	messages.addSupportedLanguages('pt-BR');

	$.AppContext.vars.contextPath = "";
	$.AppContext.vars.painel      = "content-body";
	$.AppContext.vars.dialog      = "defaultDialog";
	$.AppContext.vars.language    = messages.getLanguage();
	
</script>
