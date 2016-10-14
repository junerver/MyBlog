<?php
namespace Home\Controller;

use Think\Controller;

class ArticleController extends Controller
{


    /**
     * 文章详情
     * http://localhost/wordpress/API/Home/Article/getPostById/articleId/5
     */
    public function getPostById()
    {

        $articleId = I('articleId');

        if ($articleId <= 0) {

            $this->jsonReturn(null, '找不到该文章', 0);
            return;
        }
        $articleModel = M('posts');

        $article = $articleModel->field("id,post_content,post_title,post_date")->where(array("ID" => $articleId, "post_status" => "publish"))->find();
        if ($article != null) {
            // $article = $this->transferHtml($article);

            // $this->jsonReturn(array('article' => $article), '读取成功', 1);
            $this->ajaxReturn(array($article));
        } else {
            $this->jsonReturn(null, '找不到该文章', 0);
        }
        return;
    }

    //使用json-api的方法直接为取出的数据添加html标签
    public function transferHtml($article){
        $url="http://junerver.duapp.com/index.php/archives/".$article["id"]."?json=1";
//        $url="http://localhost/wordpress/archives/".$article["id"]."?json=1";
        //转型为关联数组
        $articleJson = json_decode(https_request($url, null),true);
        $article["post_content"]=$articleJson["post"]["content"];
        return $article;
    }

    /**
     * 获取全部文章
     * http://localhost/wordpress/API/Home/Article/getAllPosts/
     */
    public function getAllPosts()
    {
        $articleModel = M('posts');
        $articles = $articleModel->field("id,post_content,post_title,post_date")->where(array("post_status" => "publish"))->select();
        if ($articles!=null) {
            for ($i=0;$i<sizeof($articles);$i++) {
                $articles[$i] = $this->transferHtml($articles[$i]);
            }
            // $this->jsonReturn(array('articles' => $articles), '读取成功', 1);
            $this->ajaxReturn($articles);
        }else {
            $this->jsonReturn(null, '找不到该文章', 0);
        }
    }

    /**
     * 获取最后一次日期
     * http://localhost/wordpress/API/Home/Article/getLastDate/
     */
    public function getLastDate()
    {
        $articleModel = M('posts');
        $dates = $articleModel->field("post_date")->where(array("post_status" => "publish"))->select();
        dump($dates);
    }

    /**
     * 获取ID之后的所有posts
     * http://localhost/wordpress/API/Home/Article/getPostsAfterId/articleId/1
     */
    public function getPostsAfterId()
    {
        $articleId = I('articleId');
        $articleModel = M('posts');
        $condition["post_status"] = array('EQ', 'publish');
        $condition["ID"] = array('GT', $articleId);
        $articles = $articleModel->field("id,post_content,post_title,post_date")->where($condition)->select();
        if ($articles!=null) {
            $this->ajaxReturn($articles);
        }else {
            $this->jsonReturn(null, '找不到该文章', 0);
        }
    }

    /**
     * 获取文章总数
     * http://localhost/wordpress/API/Home/Article/getPostsCount/
     */
    public function getPostsCount()
    {
        $articleModel = M('posts');
        $count = $articleModel->where(array("post_status" => "publish"))->count();
        $this->jsonReturn($count, '读取成功', 1);
    }

    /**
     * 获取服务器的版本信息，通过最终修改日期与文章总数
     * http://localhost/wordpress/API/Home/Article/getServerDbVersion/
     */
    public function getServerDbVersion(){
        $articleModel = M('posts');
        $articles = $articleModel->field('post_modified')->where(array("post_status" => "publish"))->select();

        $modify_times=array();
        foreach ($articles as $key => $value) {
            $modify_times[$key]=strtotime($value['post_modified']);
        }

        //排序，取最大值作为版本号
        rsort($modify_times);

        $count=$articleModel->where(array("post_status" => "publish"))->count();


        $this->ajaxReturn(array("version"=>$modify_times[0],"count"=>$count));
    }


    /**
     * 返回客户端统一格式
     * @param  任意格式 $data 返回的数据
     * @param  字符串 $msg 请求返回的消息
     * @param  整型 $status 请求返回的状态码
     */
    public function jsonReturn($data, $msg, $status)
    {

        // 如果data是空，则处理为空字典
        if (!$data) {
            $data = (object)null;
        }
        $returnData['data'] = $data;
        $returnData['msg'] = $msg;
        $returnData['status'] = $status;
        $this->ajaxReturn($returnData, 'JSON');
    }


}