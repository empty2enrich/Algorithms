package com.leetcode.dataStructure.impl;

import com.leetcode.dataStructure.Color;
import com.leetcode.dataStructure.interfaces.Node;
import com.leetcode.dataStructure.interfaces.Tree;

//红黑树
public class RBTree implements Tree{
    private RBNode root;//树的根节点
    private RBNode leaf;//叶节点

    public RBTree(){////构造方法
        this.root =null;
        this.leaf = new RBNode();
    }

    /***
     * 向树中插入一个节点
     * @param insertedNode 待插入的节点
     * @return boolean 是否插入成功
     */
    public boolean insert(RBNode insertedNode) {
        //树中无节点
        if (this.root == null) this.root = insertedNode;
        //树中有节点
        else insertNode(this.root,insertedNode);
        //修复红黑树性质
        return fix_up_insert(insertedNode);
    }

    /***
     * 从当前节点 curNode 出插入待插入节点 insertedNode
     * @param curNode
     * @param insertedNode
     * @return boolean 插入是否成功
     */
    private boolean insertNode(RBNode curNode,RBNode insertedNode){
        if (curNode == null) { //当前节点为 null ,说明是根节点
            this.root = insertedNode;
            return true;
        }

        //当前节点为 null
        boolean isCurValLess = curNode.getVal()< insertedNode.getVal();
        RBNode childNode = isCurValLess ? curNode.getLeftChild() : curNode.getRightChild();

        if(childNode!=null) return insertNode(childNode,insertedNode);
        else return curNode.insertChild(insertedNode,this.leaf,isCurValLess);

    }

    /***
     * 在插入新节点后恢复红黑树性质
     * @param insertedNode
     * @return
     */
    private boolean fix_up_insert(RBNode insertedNode){
        //case 0 : insertedNode is root
        if (this.root == insertedNode){
            insertedNode.setColorBlack();
            return true;
        }
        // case 1 : insertedNode.parent.color = Red
        if (Color.RED.equals(insertedNode.getParent().getCol())){
            if (Color.RED.equals(insertedNode.getParent().getSiblingNode().getCol())){ // case 1 : 父节点和父节点的兄弟节点都为 Red
                insertedNode.getParent().setColorBlack();
                insertedNode.getParent().getSiblingNode().setColorBlack();
                insertedNode.getParent().getParent().setColorRed();
                return fix_up_insert(insertedNode.getParent().getParent());
            }else{
                if(insertedNode.getParent() == insertedNode.getParent().getParent().getLeftChild()){ // case 2.1 : 父节点为 爷节点左子节点
                    if (insertedNode != insertedNode.getParent().getLeftChild()){ // case 2.1.1 : insertedNode 是父节点的右子节点
                        left_rotate(insertedNode.getParent());
                        return fix_up_insert(insertedNode.getLeftChild());
                    }else{ //case 2.1.2 : insertedNode 是父节点的左子节点
                        right_rotate(insertedNode.getParent().getParent());
                        insertedNode.getParent().getRightChild().setColorRed();
                        if (this.root == insertedNode.getSiblingNode()) this.root = insertedNode.getParent();
                        return fix_up_insert(insertedNode.getParent());
                    }
                }else{ // case 2.2 父节点为爷节点右子节点
                    if (insertedNode != insertedNode.getParent().getRightChild()){ //case 2.2.1 : insertedNode 是父节点的左子节点
                        left_rotate(insertedNode.getParent());
                        return fix_up_insert(insertedNode.getRightChild());
                    }else{  //insertedNode 是父节点的右子节点
                        left_rotate(insertedNode.getParent().getParent());
                        insertedNode.getSiblingNode().setColorRed();
                        if (this.root == insertedNode.getSiblingNode()) this.root = insertedNode.getParent();
                        return  fix_up_insert(insertedNode.getParent());
                    }
                }
            }

        }
        return true;
    }

    /***
     *
     * @param node 待查找的节点,查询 tree 中是否有节点 tree.node.val = mnode.val
     * @return bolean 节点值是否在树中
     */
    public boolean search(RBNode node) {

        return false;
    }

    public boolean search(RBNode tree,RBNode searchNode){
        if tree==null return false;
        if(tree.compareTo(searchNode)>0 && !tree.leftChildIs(this.leaf)) return search(tree.getLeftChild(),searchNode);
        else if(tree.compareTo(searchNode)<0 && !tree.rightChildIs(this.leaf)) return search(tree.getRightChild,searchNode);
        else if(tree.compareTo(searchNode) == 0) return true;
        else return false;
    }

    /***
     * 删除节点 (删除树中值与 node.val 的一个节点)
     * @param deletedNote 待删除节点 （这里传Node 为了增加可扩展性, value 类型不写死）
     * @return boolean 是否删除成功
     */
    public boolean delete(RBNode deletedNote) throws Exception {
        delete(this.root,deletedNote);
        return true;
    }

    /**
     * 删除时左右子树不为空时，以右子树中最小值顶替被删除节点位置
     * @param curNode 删除时从根节点开始遍历到的节点
     * @param deletedNode 将被删除节点
     * @return
     * @throws Exception
     */
    public boolean delete(RBNode curNode,RBNode deletedNode) throws Exception {
        if(curNode == null) throw new Exception("待删除树为 null ,删除失败！");
        if(curNode.compareTo(deletedNode)>0){
            if (curNode.leftChildIs(this.leaf)) throw new Exception("删除节点在树中不存在！");
            else return delete(curNode.getLeftChild(),deletedNode);
        }else if(curNode.compareTo(deletedNode)<0){
            if (curNode.rightChildIs(this.leaf)) throw new Exception("删除节点在树中不存在");
            else return  delete(curNode.getRightChild(),deletedNode);
        }else{
            if(curNode.leftChildIs(this.leaf)){
                if (curNode == root){
                    this.root = curNode.getRightChild();
                    curNode.getRightChild().setParent(null);
                    return delete_fix_up(curNode.getRightChild(),curNode.getCol());
                }else{
//                    curNode.getParent().set
//                    curNode.getRightChild().setParent(curNode.getParent());
                    deleteLeftChildIsLeafNode(curNode);
                    return delete_fix_up(curNode.getRightChild(),curNode.getCol());
                }
            }else{
                RBNode leftChildIsLeaf = findLeftChildIsLeafNode(curNode.getRightChild());
                curNode.setVal(leftChildIsLeaf.getVal());
                deleteLeftChildIsLeafNode(leftChildIsLeaf);
                return delete_fix_up(leftChildIsLeaf,leftChildIsLeaf.getCol());

            }
        }
    }

    public boolean deleteLeftChildIsLeafNode(RBNode leftChildIsLeaf){
        leftChildIsLeaf.getRightChild().setParent(leftChildIsLeaf.getParent());
        if (leftChildIsLeaf.isLeftChild()) leftChildIsLeaf.getParent().setLeftChild(leftChildIsLeaf.getRightChild());
        else leftChildIsLeaf.getParent().setRightChild(leftChildIsLeaf.getRightChild());
        return true;
    }


    /**
     * @param fixUpNode 修复红黑树性质的节点
     * @return
     */
    public boolean delete_fix_up(RBNode fixUpNode,Color deletedColor){
        if (deletedColor==Color.RED) return true;
        else{
            if (fixUpNode.getCol()==Color.RED){
                fixUpNode.setColorBlack();
                return true;
            }else{
                if (fixUpNode == root) return true;
                else {
                    if (fixUpNode.getSiblingNode().getCol() == Color.RED){
                        left_rotate(fixUpNode.getParent());
                        exchangeColor(fixUpNode.getParent(),fixUpNode.getParent().getParent());
                        return delete_fix_up(fixUpNode,deletedColor);
                    }else if (fixUpNode.getSiblingNode().getLeftChild().getCol() == Color.RED){
                        right_rotate(fixUpNode.getSiblingNode());
                        exchangeColor(fixUpNode.getSiblingNode(),fixUpNode.getSiblingNode().getRightChild());
                        return delete_fix_up(fixUpNode,deletedColor);
                    }else if (fixUpNode.getSiblingNode().getRightChild().getCol() == Color.RED){
                        left_rotate(fixUpNode.getParent());
                        exchangeColor(fixUpNode.getParent(),fixUpNode.getParent().getParent());
                        fixUpNode.getParent().getSiblingNode().setColorBlack();
                        return true;
                    }else if (fixUpNode.getSiblingNode().getLeftChild().getCol()== Color.BLACK && fixUpNode.getSiblingNode().getRightChild().getCol()==Color.BLACK){
                        left_rotate(fixUpNode.getParent());
                        exchangeColor(fixUpNode.getParent(),fixUpNode.getParent().getParent());
                        fixUpNode.getParent().setColorRed();
                        return delete_fix_up(fixUpNode.getParent().getParent(),deletedColor);
                    }
                }
            }
        }
        return true;
    }

    public RBNode findLeftChildIsLeafNode(RBNode rbNode) {
        if(rbNode.leftChildIs(this.leaf)) return rbNode;
        else return findLeftChildIsLeafNode(rbNode);
    }

    /**
     * 当前树中节点 value 更大 并且左子树不是叶节点
     * @return
     */
//    public boolean curValueLargerAndLeftChildIsNotLeaf(Node curNode,Node deletedNode){
//        return (curNode.getVal() > deletedNode.getVal() && curNode.getLeftChild()!=this.leaf) ? true:false;
//    }

    /***
     * 左旋
     * @param curNode
     */
    private void left_rotate(RBNode curNode){
        if (curNode.getRightChild()!=this.leaf){
            //右子树成为父节点子树
            if(curNode == curNode.getParent().getLeftChild()) curNode.getParent().setLeftChild(curNode.getRightChild());
            else curNode.getParent().setRightChild(curNode.getRightChild());
            //将右节点父节点指向当前节点父节点
            curNode.getRightChild().setParent(curNode.getParent());
            //当前节点变为右子树的左子节点,同时当前节点右节点指向右子节点的左节点
            curNode.setParent(curNode.getRightChild());
            curNode.setRightChild(curNode.getRightChild().getLeftChild());

            curNode.getParent().setLeftChild(curNode);
            curNode.getRightChild().setParent(curNode);

            if (curNode==root) root = curNode.getParent();
        }
    }

    /***
     * 右旋
     * @param curNode
     */
    public void right_rotate(RBNode curNode){
        if (curNode.getLeftChild()!=this.leaf){
            //左子树成为父节点子树
            if(curNode == curNode.getParent().getLeftChild()) curNode.getParent().setLeftChild(curNode.getRightChild());
            else curNode.getParent().setRightChild(curNode.getRightChild());
            //将左节点父节点指向当前节点父节点
            curNode.getLeftChild().setParent(curNode.getParent());
            //当前节点变为左子树的左子节点,同时当前节点左节点指向左子节点的右子节点
            curNode.setParent(curNode.getLeftChild());
            curNode.setLeftChild(curNode.getLeftChild().getRightChild());

            curNode.getParent().setRightChild(curNode);
            curNode.getLeftChild().setParent(curNode);

            if (curNode==root) root = curNode.getParent();
        }
    }

    public boolean exchangeColor(RBNode rbNodeOne,RBNode rbNodeTwo){
        Color tmp = rbNodeOne.getCol();
        rbNodeOne.setCol(rbNodeTwo.getCol());
        rbNodeTwo.setCol(tmp);
        return true;
    }

    @Override
    public boolean insert(Node node) {
        return false;
    }

    @Override
    public boolean search(Node node) {
        return false;
    }

    @Override
    public boolean delete(Node node) {
        RBNode rb = new RBNode();
//        delete(rb);
        return false;
    }
}
