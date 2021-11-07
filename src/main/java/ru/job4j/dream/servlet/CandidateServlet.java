package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.DbStore;
import ru.job4j.dream.store.MemStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class CandidateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String delete = req.getParameter("delete");
        if (delete != null && "true".equals(delete)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Candidate candidate = DbStore.instOf().findCandidateById(id);
            String fileName = candidate.getFileName();
            File file = new File("c:\\images\\" + File.separator + fileName);
            if (file.exists()) {
                file.delete();
            }
            DbStore.instOf().removeCandidate(id);
        }
        req.setAttribute("candidates", DbStore.instOf().findAllCandidates());
        req.getRequestDispatcher("candidates.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        DbStore.instOf().saveCandidate(
                new Candidate(
                        Integer.valueOf(req.getParameter("id")),
                        req.getParameter("name")
                )
        );
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
