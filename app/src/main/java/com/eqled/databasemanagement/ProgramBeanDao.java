package com.eqled.databasemanagement;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.eqled.bean.ProgramBean;
import com.j256.ormlite.dao.Dao;

/**
 * Created by Administrator on 2016/6/17.
 */
public class ProgramBeanDao {
    private Context context;
    private Dao<ProgramBean, Integer> programBeanIntegerDao;
    private DatabaseHelper helper;

    public ProgramBeanDao(Context context) {
        this.context = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            programBeanIntegerDao = helper.getDao(ProgramBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param programBean
     */
    public void add(ProgramBean programBean) {
        try {
            programBeanIntegerDao.create(programBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }//...other operations

    /**
     * 获取所有的节目
     *
     * @return
     */
    public List<ProgramBean> getListAll() {
        try {
            return programBeanIntegerDao.queryForAll();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定的节目
     *
     * @return
     */
    public ProgramBean get(int id) {
        try {
            return programBeanIntegerDao.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 删除指定条目
     */
    public void delete(int id) {
        try {
            programBeanIntegerDao.deleteById(id);
            new TextBeanDao(context).DeleteALll(id); //删除节目下所有的文本窗体
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新指定条目
     */
    public void update(ProgramBean programBean) {
        try {
            programBeanIntegerDao.update(programBean);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
