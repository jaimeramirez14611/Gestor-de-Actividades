<%@ page import="daoImp.UsuarioDaoImp" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/Proyectos" var="registerUrl" />
<c:set var="data" scope="application" value="${datos}"/>

<%--header--%>
<jsp:include page="../components/header.jsp"/>

<%-- navbar --%>
<jsp:include page="../components/navbar.jsp"/>

<div class="content">
	<div class="container-fluid col-10 offset-1">
		<div class="row">
			<div class="col-12">
				<nav aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="Proyectos?action=index">Administración de Proyectos</a></li>
						<li class="breadcrumb-item active" aria-current="page">Edición</li>
					</ol>
				</nav>
			</div>
		</div>

		<div class="container col-12 col-md-9 offset-md-1">
			<form method="POST" action="${registerUrl}" class="needs-validation pt-3" novalidate>
				<input type="hidden" name="option" value="update">
				<input type="hidden" name="redirect" value="false">

				<c:forEach items="${data}" var="proyectos">
					<input type="hidden" name="id" value="${proyectos.id}">
					<div class="form-row">
						<div class="form-group col-md-12">
							<label><strong class="asterisk-required">*</strong> Proyecto</label>
							<input type="text" name="nombre_proyecto" class="form-control" value="${proyectos.nombre_proyecto}" required>
							<div class="invalid-feedback">
								Debe ingresar un nombre para el proyecto.
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-12">
							<label><strong class="asterisk-required">*</strong> Descripcion</label>
							<input type="text" name="descripcion" class="form-control" value="${proyectos.descripcion}"required>
							<div class="invalid-feedback">
								Debe ingresar una descripcion para el proyecto.
							</div>
						</div>
					</div>

					<div class="form-row">
						<div class="form-group col-md-6">
							<label><strong class="asterisk-required">*</strong> Usuario</label>
							<%
								try {
									List<Map<String, String>> users = UsuarioDaoImp.getInstance().findAllUsuario();
									request.setAttribute("users", users);
								} catch (Exception e) {
									e.printStackTrace();
								}
							%>
							<select name="id_usuario" id="selectUserProject" class="custom-select" style="width: 100%" required>
								<c:forEach items="${users}" var="user">
									<c:choose>
										<c:when test="${user.id == proyectos.id_usuario}">
											<option selected value="${user.id}"><c:out value="${user.nombres}, ${user.apellidos}"/></option>
										</c:when>
										<c:otherwise>
											<option value="${user.id}"><c:out value="${user.nombres}, ${user.apellidos}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<div class="invalid-feedback">
								Debe seleccionar un usuario para el proyecto.
							</div>
						</div>
					</div>
				</c:forEach>

				<section>
					<label class="required"><strong>* Campo requerido</strong></label>
				</section>

				<div class="row pt-3">
					<a href="Proyectos?action=index" class="col-12 col-md-3 offset-md-5 mr-3 btn btn-danger" role="button" aria-pressed="true">Cancelar</a>
					<button type="submit" class="col-12 col-md-3 mr-3 btn btn-success">Actualizar</button>
				</div>
			</form>
		</div>
	</div>
</div>
<%-- Footer --%>
<jsp:include page="../components/footer.jsp"/>
