package controller;

import dao.ProyectosActividadesDao;
import daoImp.ProyectosActividadesDaoImp;
import model.ProyectosActividades;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ControllerProyectosActividades
 */
@WebServlet(name= "ProyectosActividades", urlPatterns="/ProyectosActividades")
public class ControllerProyectosActividades extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProyectosActividadesDao pado = ProyectosActividadesDaoImp.getInstance();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerProyectosActividades() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String action = request.getParameter("action");

		switch (action) {
			case "index":
				request.setAttribute("list", this.listar());

				request.getRequestDispatcher("views/proyectos_actividades/ListProyectosActividades.jsp").forward(request, response);
				break;
			case "add":
				//request.setAttribute("listProyectosActividades", this.listar());
				//request.getRequestDispatcher("views/proyectos_actividades/List.jsp").forward(request, response);
				break;
			default:
				response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String option = request.getParameter("option");


		boolean redirect = request.getParameter("redirect").equals("true");

		Short id = request.getParameter("id") != null ?Short.parseShort(request.getParameter("id")) : 0;
		String proyecto = request.getParameter("proyecto");
		String username = request.getParameter("username");
		String actividad= request.getParameter("actividad");
		byte prioridad = request.getParameter("priroridad") != null ? Byte.parseByte(request.getParameter("prioridad")) : 0;
		byte estado = request.getParameter("estado") != null ? Byte.parseByte(request.getParameter("estado")) : 0;
		String entrega= request.getParameter("Entrega");
		short id_usuario = request.getParameter("id_usuario") != null ? Short.parseShort(request.getParameter("id_usuario")) : 0;
		short id_poyecto = request.getParameter("id_proyecto") != null ? Short.parseShort(request.getParameter("id_proyecto")) : 0;

		System.out.println(id);
		System.out.println(prioridad);
		switch (option) {
		case "add":
			
			break;
		case "update":
			if (redirect) {
				request.setAttribute("datos", this.crearLista(id, actividad, prioridad, estado, entrega, id_usuario, id_poyecto));
				request.getRequestDispatcher("views/proyectos_actividades/UpdateProyectosActividades.jsp").forward(request, response);
			} else {
				try {
					this.update(this.crearLista(id, actividad, prioridad, estado, entrega, id_usuario, id_poyecto));
				} catch (Exception e) {
					e.printStackTrace();
				}

				String contextPath= "";
				response.sendRedirect(response.encodeRedirectURL(contextPath + "/InfusionActivity_war_exploded/TipoUsuario?action=index"));
			}
			break;

		default:
			break;
		}
	}

	/**
	 *  METODOS
	 * @return
	 */

	public List<Map<String, String>> listar() {
		List<Map<String, String>> proyecos_actividades = null;
				
		try {
			proyecos_actividades = pado.findAll();

			for (Map<String, String> actividad:
				 proyecos_actividades) {

				Map<String, String> map = this.getTimeRemaining(actividad.get("entrega"));
				actividad.put("restante", map.get("time") + " " + map.get("units"));
			}

			return  proyecos_actividades;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, String> getTimeRemaining(String fecha_entrega) {
		Map<String, String> entrega = new HashMap<>();

		String time;
		String units;

		LocalDate fentrega = LocalDate.parse(fecha_entrega, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		LocalDate current_date = LocalDate.now();

		Period period = Period.between(current_date, fentrega);
		int days = period.getDays();

		if (days <= 0) {
			time = "0";
			units = "Finalizada";
		} else {
			time = String.valueOf(days);
			units = (days > 1 ) ? "dias" : "dia";
		}

		entrega.put("units", units);
		entrega.put("time", time);

		return entrega;
	}

	private  List<ProyectosActividades> crearLista(short id, String actividad, byte prioridad, byte estado, String entrega, short id_usuario, short id_proyecto){
		//crear la lista
		List<ProyectosActividades> list_tipo_usuario = new ArrayList<>();

		//Objeto
		ProyectosActividades tipo_usuario  = new ProyectosActividades(id, actividad, prioridad, estado, entrega, id_usuario, id_proyecto);

		//add to list
		list_tipo_usuario.add(tipo_usuario);

		return list_tipo_usuario;
	}

	private String  save(List<ProyectosActividades> list) throws Exception{
		//construccion del objeto
		for (ProyectosActividades pa:
				list) {
			pado.save(pa);
		}

		return "ok";
	}

	private String  update(List<ProyectosActividades> list) throws Exception{
		//construccion del objeto
		for (ProyectosActividades pa:
			 list) {
			pado.update(pa);
		}

		return "ok";
	}
}