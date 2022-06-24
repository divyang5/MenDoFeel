package com.example.mendofeel

import android.content.Context
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MenDoAdapter(val context:Context,val MenDoList: MenDo): RecyclerView.Adapter<MenDoAdapter.ViewHolder>() {
    class ViewHolder(itemview:View): RecyclerView.ViewHolder(itemview) {

        var profile_image: ImageView
        var username: TextView
        var timeago: TextView
        var questiontext: TextView
        var post_photo: ImageView
        var likescount: TextView
        var commentscount: TextView
        var likeImage: ImageView
        var commentimage: ImageView
        var shareimage: ImageView
        var poll1:SeekBar
        var poll1per:TextView
        var poll1perText: TextView
        var pollLayout:ConstraintLayout
        var poll2:SeekBar
        var poll2per:TextView
        var poll2perText: TextView
        var popupMenu: ImageView


        init {
            profile_image=itemview.findViewById(R.id.profile_image)
            username=itemview.findViewById(R.id.username)
            timeago=itemview.findViewById(R.id.timeAgo)
            questiontext=itemview.findViewById(R.id.questiontext)
            post_photo=itemview.findViewById(R.id.post_photo)
            likescount=itemview.findViewById(R.id.likescount)
            commentscount=itemview.findViewById(R.id.commentscount)
            likeImage=itemview.findViewById(R.id.likeImage)
            commentimage=itemview.findViewById(R.id.commentimage)
            shareimage=itemview.findViewById(R.id.shareimage)
            poll1=itemview.findViewById(R.id.poll1)
            poll1per=itemview.findViewById(R.id.poll1per)
            poll1perText=itemview.findViewById(R.id.poll1PerText)
            pollLayout=itemview.findViewById(R.id.pollLayout)
            poll2=itemview.findViewById(R.id.poll2)
            poll2per=itemview.findViewById(R.id.poll2per)
            poll2perText=itemview.findViewById(R.id.poll2PerText)
            popupMenu=itemview.findViewById(R.id.popmenu)


        }





    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemview=LayoutInflater.from(context).inflate(R.layout.sample_post,parent,false)
        return ViewHolder(itemview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder?.popupMenu?.setImageResource(R.drawable.ic_menu)
        Glide.with(context).load(MenDoList.results[position].profile_photo).placeholder(R.drawable.ic__user).into(holder.profile_image)


        if (MenDoList.results[position].fullname!=null) {
            holder?.username?.text= MenDoList.results[position]?.fullname?.toString()
        }

        else{
            holder.username.text = "Anonymous User"
        }

        if(MenDoList.results[position].type.equals("Forum")){
            holder?.timeago?.text=MenDoList.results[position]?.publishtime
            holder?.likeImage?.setImageResource(R.drawable.ic__like_outline)
            holder?.commentimage?.setImageResource(R.drawable.ic_comment)
            holder?.shareimage?.setImageResource(R.drawable.ic_share)
            holder?.likescount?.text=MenDoList.results[position]?.likescount?.toString()
            holder?.commentscount?.text=MenDoList.results[position]?.commentscount?.toString()
            holder?.pollLayout?.visibility=View.GONE
            if(MenDoList.results[position].post_photo!=null) {
                Glide.with(context).load(MenDoList.results[position].post_photo).into(holder.post_photo)
            }else{
                holder.post_photo.visibility=View.GONE
            }

            holder?.questiontext?.text=MenDoList.results[position]?.question?.toString()



            holder?.questiontext?.setOnClickListener(object : DoubleClickListner(){
                override fun onDoubleClick(v: View?) {
                    holder?.likeImage?.setImageResource(R.drawable.ic_line_red)
                    holder?.likescount?.text=(holder?.adapterPosition?.let {
                        MenDoList.results?.get(it)?.likescount?.toString()?.toInt()
                            ?.plus(1)
                    })?.toString()
                }
            })
            holder?.post_photo?.setOnClickListener(object : DoubleClickListner(){
                override fun onDoubleClick(v: View?) {
                    holder?.likeImage?.setImageResource(R.drawable.ic_line_red)
                    holder?.likescount?.text=(holder?.adapterPosition?.let {
                        MenDoList.results?.get(it)?.likescount?.toString()?.toInt()
                            ?.plus(1)
                    })?.toString()
                }
            })

            holder?.likeImage?.setOnTouchListener(object :View.OnTouchListener{
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    holder?.likeImage?.setImageResource(R.drawable.ic_line_red)
                    holder?.likescount?.text=(holder?.adapterPosition?.let {
                        MenDoList.results?.get(it)?.likescount?.toString()?.toInt()
                            ?.plus(1)
                    })?.toString()
                    return false
                }
            })

        }

        else{
            //set the data for poll 1
            holder?.poll1perText?.text= MenDoList?.results?.get(position)?.choices?.get(0)?.choice_text.toString()
            holder?.poll1per?.text= MenDoList?.results?.get(position)?.choices?.get(0)?.percentage.toString() + "%"
            holder?.poll1?.progress= MenDoList?.results?.get(position)?.choices?.get(0)?.percentage.toString().toInt()

            //set the data for poll 2
            holder?.poll2perText?.text= MenDoList?.results?.get(position)?.choices?.get(1)?.choice_text.toString()
            holder?.poll2per?.text= MenDoList?.results?.get(position)?.choices?.get(1)?.percentage.toString() + "%"
            holder?.poll2?.progress= MenDoList?.results?.get(position)?.choices?.get(1)?.percentage.toString().toInt()



            holder?.timeago?.text=MenDoList.results[position]?.publish
            holder.post_photo.visibility=View.GONE
            holder?.questiontext?.text=MenDoList.results[position]?.question_text?.toString()

            //on touch for poll1 and poll 2

            holder?.poll1?.setOnTouchListener(object :View.OnTouchListener{
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    var p1=MenDoList?.results?.get(holder.adapterPosition)?.choices
                    var per=findPercentage(p1?.get(0)?.votes.toString().toInt()+1 ,p1?.get(1)?.votes.toString().toInt())
                    holder?.poll1per?.text= per.toString()
                    holder?.poll1?.progress= per
                    holder?.poll2per?.text= (100-per).toString()
                    holder?.poll2?.progress= 100-per

                    holder?.poll2?.setOnTouchListener(object :View.OnTouchListener{
                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                            return true
                        }
                    })

                    return true
                }
            })

            holder?.poll2?.setOnTouchListener(object :View.OnTouchListener{
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    var p1=MenDoList?.results?.get(holder.adapterPosition)?.choices
                    var per=findPercentage(p1?.get(1)?.votes.toString().toInt()+1 ,p1?.get(0)?.votes.toString().toInt())
                    holder?.poll2per?.text= per.toString()
                    holder?.poll2?.progress= per
                    holder?.poll1per?.text= (100-per).toString()
                    holder?.poll1?.progress= 100-per


                    holder?.poll1?.setOnTouchListener(object :View.OnTouchListener{
                        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                            return true
                        }
                    })
                    return true
                }
            })




        }


        holder?.popupMenu?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val popupMenu = PopupMenu(context, p0, Gravity.END)
                popupMenu.menu.add("Edit")
                popupMenu.menu.add("Delete")
                popupMenu.show()
            }

        })


    }

    private fun findPercentage(i: Int, i1: Int): Int {

        var per:Int
        per=(i*100)/(i+i1)
        return per
    }

    override fun getItemCount(): Int {
        return MenDoList.results.size
    }
}

abstract class DoubleClickListner: View.OnClickListener {

    var lastClickTime:Long=0
    override fun onClick(p0: View?) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(p0)
        }
        lastClickTime = clickTime

    }

    abstract fun onDoubleClick(v:View?)
    companion object{
        private const val DOUBLE_CLICK_TIME_DELTA:Long=300
    }
}
