package br.edu.ufabc.fisicaludica.view.fragments


import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import br.edu.ufabc.fisicaludica.R
import br.edu.ufabc.fisicaludica.databinding.FragmentGameInputWindowBinding
import br.edu.ufabc.fisicaludica.model.domain.GameLevel
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel

/**
 * Fragment for the game input data stage.
 */
class GameInputWindowFragment : Fragment() {
    private lateinit var binding: FragmentGameInputWindowBinding
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameInputWindowBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackground()
        bindEvents()
    }

    override fun onResume() {
        super.onResume()
        setElementsState()
    }

    private fun setBackground() {
        viewModel.clickedMapId.value?.let {
            val gameLevel: GameLevel = viewModel.getMapById(it)
            binding.fragmentGameMapLayout.background =
                Drawable.createFromStream(viewModel.getMapBackgroundInputStream(gameLevel),gameLevel.backgroud)
        }
    }
    private fun setElementsState() {

        requireView().post {
            val fragmentHeight = requireView().height //height is ready
            val fragmentWidth = requireView().width
            Log.d("resolução", "temos ${fragmentWidth} ${ fragmentHeight}")
            positionateElements(fragmentWidth, fragmentHeight)
        }

    }

    private fun positionateElements(fragmentWidth: Int, fragmentHeight: Int) {
        viewModel.clickedMapId.value?.let {
            val gameLevel: GameLevel = viewModel.getMapById(it)

            binding.gameFragmentTextviewCoordinates.text =
                String.format(getString(R.string.game_fragment_coordinate), gameLevel.elementsPosition.posXTarget - gameLevel.elementsPosition.posXLauncher, gameLevel.elementsPosition.posYTarget)

            binding.gameFragmentSlideBarVelocity.value = gameLevel.worldAtributtes.initialVelocity.toFloat()
            binding.gameFragmentSlideBarTheta.value = gameLevel.worldAtributtes.initialAngleDegrees.toFloat()
            binding.gameFragmentSlideBarVelocity.isEnabled = gameLevel.worldAtributtes.isVelocityVariable
            binding.gameFragmentSlideBarTheta.isEnabled = gameLevel.worldAtributtes.isAngleVariable

            binding.gameFragmentSlideBarTheta.valueFrom = (gameLevel.worldAtributtes.initialAngleDegrees % 10).toFloat()
            binding.gameFragmentSlideBarTheta.valueTo = (gameLevel.worldAtributtes.initialAngleDegrees + 5*10).toFloat()
            binding.gameFragmentSlideBarVelocity.valueFrom = (gameLevel.worldAtributtes.initialVelocity % 10).toFloat()
            binding.gameFragmentSlideBarVelocity.valueTo = (gameLevel.worldAtributtes.initialVelocity % 10 + 4*10).toFloat()

            val screenScale = fragmentWidth / gameLevel.widthMeters
            val launcherWidthPixel = (screenScale).times(gameLevel.projectileLauncherWidht)
            val layoutParamsLauncher = binding.gameFragmentCannonball.layoutParams as ConstraintLayout.LayoutParams

            layoutParamsLauncher.horizontalBias =  (gameLevel.elementsPosition.posXLauncher - gameLevel.projectileLauncherWidht/2).toFloat().div(gameLevel.widthMeters).toFloat()
            layoutParamsLauncher.verticalBias = (
                    (gameLevel.groundPosition.times(fragmentHeight)).minus(gameLevel.elementsPosition.posYLauncher.times(screenScale).plus(gameLevel.projectileLauncherWidht/2))
                    ).div(fragmentHeight).toFloat()


            layoutParamsLauncher.width = launcherWidthPixel.toInt()
            layoutParamsLauncher.height = launcherWidthPixel.toInt()


            val matrix = Matrix()
            matrix.postRotate( -gameLevel.elementsPosition.targetRotation.toFloat())
            val bitmap = (binding.gameFragmentTarget.drawable as BitmapDrawable).bitmap
            val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            binding.gameFragmentTarget.setImageBitmap(rotatedBitmap)


            val layoutParamsTarget = binding.gameFragmentTarget.layoutParams as ConstraintLayout.LayoutParams

            layoutParamsTarget.width = screenScale.times(gameLevel.targetHeight).toInt()
            layoutParamsTarget.height = screenScale.times(gameLevel.targetHeight).toInt()

            layoutParamsTarget.horizontalBias = (gameLevel.elementsPosition.posXTarget.minus(gameLevel.targetHeight/2)).toFloat().div(gameLevel.widthMeters.toFloat())
            Log.d("bias target", "${layoutParamsTarget.horizontalBias}")
            layoutParamsTarget.verticalBias = (
                    (gameLevel.groundPosition.times(fragmentHeight)).minus(gameLevel.elementsPosition.posYTarget.times(screenScale).plus(gameLevel.targetHeight/2))
                    ).div(fragmentHeight).toFloat()

        }

    }

    private fun bindEvents() {
        binding.gameFragmentPlayBotton.setOnClickListener {
            GameInputWindowFragmentDirections.showGameScene(binding.gameFragmentSlideBarVelocity.value,
                binding.gameFragmentSlideBarTheta.value)
                .let {
                    findNavController().navigate(it)
                }
        }
        binding.gameFragmentSlideBarVelocity.addOnChangeListener { _, value, _ ->
            val velocity = "$value m/s"
            binding.gameFragmentTextviewVelocity.text = velocity
        }
        binding.gameFragmentSlideBarTheta.addOnChangeListener { _, value, _ ->
            val angle = "${value}º"
            binding.gameFragmentTextviewTheta.text = angle
        }
    }
}
