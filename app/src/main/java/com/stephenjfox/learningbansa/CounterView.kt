package com.stephenjfox.learningbansa

import android.view.View
import android.widget.LinearLayout
import com.brianegan.bansa.Action
import com.brianegan.bansa.Store
import java.util.UUID
import trikita.anvil.DSL.*

/**
 * Everything related to the view for any given Counter
 *
 * Created by Stephen on 9/3/2016.
 */

fun counterView(model: CounterViewModel) {
  val (counter, increment, decrement) = model

  linearLayout {
    size(FILL, WRAP)
    orientation(LinearLayout.HORIZONTAL)
    margin(0, dip(10))

    textView {
      size(0, WRAP)
      weight(1f)
      text("Counts: ${counter.toString()}")
    }

    button {
      size(WRAP, WRAP)
      padding(dip(10))
      text("+")
      onClick(increment)
    }

    button {
      size(WRAP, WRAP)
      padding(dip(5))
      text("-")
      onClick(decrement)
    }
  }
}

sealed class StateTransforms {
  data class INIT(val state: ApplicationState = ApplicationState()) : Action
  data class ADD(val counter: Counter = Counter()) : Action
  object REMOVE : Action
  data class INCREMENT(val id: UUID) : Action
  data class DECREMENT(val id: UUID) : Action
}

/**
 * build a viewModel instance that will actually serve our interaction purposes
 */
fun buildInteractiveCounterViewModel(store: Store<ApplicationState>): (Counter) -> CounterViewModel {
  return { counter ->
    val (id, value) = counter
    val increment = View.OnClickListener {
      store.dispatch(StateTransforms.INCREMENT(id))
    }

    val decrement = View.OnClickListener {
      store.dispatch(StateTransforms.DECREMENT(id))
    }

    CounterViewModel(value, increment, decrement)
  }
}

/**
 * The dumb object that has the parts to a construct a view
 */
data class CounterViewModel(val counter: Int,
                            val incrementer: View.OnClickListener,
                            val decrementer: View.OnClickListener)