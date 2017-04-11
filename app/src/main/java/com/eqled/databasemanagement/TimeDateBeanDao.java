package com.eqled.databasemanagement;

import android.content.Context;

import com.eqled.bean.ProgramBean;
import com.eqled.bean.TextBean;
import com.eqled.bean.TimeDateBean;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/17.
 */
public class TimeDateBeanDao {

    private Dao<TimeDateBean, Integer> TimeDateBeanDao;
    private DatabaseHelper helper;

    @SuppressWarnings("unchecked")
    public TimeDateBeanDao(Context context) {
        try {
            helper = DatabaseHelper.getHelper(context);
            TimeDateBeanDao = helper.getDao(TimeDateBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加一个Article
     *
     * @param
     */
    public void add(TimeDateBean timeDateBean) {
        try {
            TimeDateBeanDao.create(timeDateBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Id得到一个Article
     *
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public TimeDateBean getArticleWithUser(int id) {
        TimeDateBean article = null;
        try {
            article = TimeDateBeanDao.queryForId(id);
            helper.getDao(ProgramBean.class).refresh(article.getProgramBean());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * 通过Id得到一篇time
     *
     * @param id
     * @return
     */
    public TimeDateBean get(int id) {
        TimeDateBean article = null;
        try {
            article = TimeDateBeanDao.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * 通过ProgramId获取所有的文章
     *
     * @param userId
     * @return
     */
    public List<TimeDateBean> listByUserId(int userId) {
        try {
            return TimeDateBeanDao.queryBuilder().where().eq("program_id", userId)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 更新指定条目
     */
    public void update(TimeDateBean timeDateBean) {
        try {
            TimeDateBeanDao.update(timeDateBean);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除指定条目
     */
    public void delete(TimeDateBean timeDateBean) {
        try {
            TimeDateBeanDao.delete(timeDateBean);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 通过上层ID，删除所有文本
     *
     * @param userId
     * @return
     */
    public void DeleteALll(int userId) {
        try {
            TimeDateBeanDao.deleteBuilder().where().eq("program_id", userId)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
