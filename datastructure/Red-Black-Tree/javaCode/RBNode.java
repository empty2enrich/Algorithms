package com.leetcode.dataStructure.impl;

import com.leetcode.dataStructure.Color;
import com.leetcode.dataStructure.interfaces.Node;

//红黑树节点
public class RBNode{
    private int val;//值
    private Color col;//颜色：红、黑
	private RBNode parent;//父节点
	private RBNode leftChild;//左子节点
	private RBNode rightChild;//右子节点

    public RBNode getParent() {
        return parent;
    }

    public void setParent(RBNode parent) {
        this.parent = parent;
    }

    public RBNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(RBNode leftChild) {
        this.leftChild = leftChild;
    }

    public RBNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(RBNode rightChild) {
        this.rightChild = rightChild;
    }

    public Color getCol() {
        return col;
    }

    public void setCol(Color col) {
        this.col = col;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    //构造方法
    public RBNode(){

    }

    public RBNode(int val){
        this.val = val;
        this.col = Color.RED;
    }

    /***
     * 节点染色
     */
    public void setColorRed(){
        this.col = Color.RED;
    }

    public void setColorBlack(){
        this.col = Color.BLACK;
    }

    /***
     * 插入待插入节点 insertedNode 为当前节点 curNode 子节点
     *
     * @param insertedNode 待插入节点
     * @param leaf 树的叶节点
     * @param isLeft 是否为左节点
     * @return
     */
    public boolean insertChild(RBNode insertedNode,RBNode leaf,Boolean isLeft){
        if (isLeft)  this.setLeftChild(insertedNode);
        if (!isLeft) this.setRightChild(insertedNode);
        insertedNode.setParent(this);
        insertedNode.setLeftChild(leaf);
        insertedNode.setRightChild(leaf);
        return true;
    }

    /***
     * 获取兄弟节点
     *
     * @return
     */
    public RBNode getSiblingNode(){
        if (this == this.getParent().getLeftChild()) return this.getParent().getRightChild();
        else return this.getParent().getLeftChild();
    }

    public int compareTo(RBNode rbNode){
        if(this.val>rbNode.getVal()) return 1;
        else if (this.val==rbNode.val) return 0;
        else return -1;
    }

    public boolean leftChildIs(RBNode rbNode){
        return this.leftChild==rbNode ? true : false;
    }

    public boolean rightChildIs(RBNode rbNode){
        return this.rightChild==rbNode ? true : false;
    }

    public boolean isLeftChild(){
        return (this.parent.getLeftChild()==this) ? true : false;
    }
}
