package ru.job4j.dream.servlet;

import ru.job4j.dream.model.Candidate;
import ru.job4j.dream.store.DbStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class DeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Candidate candidate = DbStore.instOf().findCandidateById(id);
        String fileName = candidate.getFileName();
        File file = new File(DbStore.getStorePath() + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
        DbStore.instOf().removeCandidate(id);
        resp.sendRedirect(req.getContextPath() + "/candidates.do");
    }
}
