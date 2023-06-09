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
import br.edu.ufabc.fisicaludica.model.domain.GameLevelAnswer
import br.edu.ufabc.fisicaludica.viewmodel.FragmentWindow
import br.edu.ufabc.fisicaludica.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

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
        bindEvents()
        binding.progressHorizontal.visibility = View.VISIBLE
        binding.gameFragmentGameWindow.visibility = View.INVISIBLE

        viewModel.clickedMapId.value?.let {
            viewModel.getGameLevelById(it).observe(viewLifecycleOwner) { status ->
                when (status) {
                    is MainViewModel.Status.Loading -> {
                        binding.progressHorizontal.visibility = View.VISIBLE
                    }
                    is MainViewModel.Status.Success -> {
                        val gameLevel = (status.result as MainViewModel.Result.SingleGameLevel).value
                        Log.d("teste velocidade", "O game veio com velocidade = ${gameLevel.worldAtributtes.initialVelocity}")
                        setBackground(gameLevel)
                        setElementsState(gameLevel)
                        elementsPosition(
                            viewModel.fragmentResolutionWidth!!,
                            viewModel.fragmentResolutionHeight!!,
                            gameLevel)
                        setUpTargetPosition(viewModel.fragmentResolutionWidth!!,
                            viewModel.fragmentResolutionHeight!!,
                            gameLevel)
                        binding.progressHorizontal.visibility = View.GONE
                        binding.gameFragmentGameWindow.visibility = View.VISIBLE
                    }
                    is MainViewModel.Status.Failure -> {
                        Log.e("FRAGMENT", "Failed to get gameLevel ", status.e)
                        Snackbar.make(binding.root, "Failed start the game", Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressHorizontal.visibility = View.INVISIBLE
                    }
                }
            }
        }

    }



    private fun setBackground(gameLevel: GameLevel) {
        Log.d("input window", "A url do background eh ${gameLevel.backgroudUrl}")
        binding.fragmentGameMapLayout.background =
            Drawable.createFromStream(viewModel.getMapBackgroundInputStream(gameLevel),gameLevel.backgroudUrl)
    }
    private fun setElementsState(gameLevel: GameLevel) {
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
    }

    private fun elementsPosition(fragmentWidth: Int, fragmentHeight: Int, gameLevel: GameLevel) {

        val screenScale = fragmentWidth / gameLevel.widthMeters
        val launcherWidthPixel = (screenScale).times(gameLevel.projectileLauncherWidht)

        val layoutParamsLauncher = binding.gameFragmentCannonball.layoutParams as ConstraintLayout.LayoutParams

        layoutParamsLauncher.horizontalBias =
            (gameLevel.elementsPosition.posXLauncher - gameLevel.projectileLauncherWidht/8)
            .toFloat()
            .div(gameLevel.widthMeters)
            .toFloat()

        layoutParamsLauncher.verticalBias =
            (
                (gameLevel.groundPosition.times(fragmentHeight))
                .minus(gameLevel.elementsPosition.posYLauncher.times(screenScale))
                .minus(gameLevel.projectileWidth.times(screenScale)))
                .div(fragmentHeight).toFloat()


        layoutParamsLauncher.width = launcherWidthPixel.toInt()
        layoutParamsLauncher.height = launcherWidthPixel.toInt()

        binding.gameFragmentCannonball.layoutParams = layoutParamsLauncher


    }

    fun setUpTargetPosition(fragmentWidth: Int, fragmentHeight: Int, gameLevel: GameLevel) {
        val screenScale = fragmentWidth / gameLevel.widthMeters

        val matrix = Matrix()
        matrix.postRotate( -gameLevel.elementsPosition.targetRotation.toFloat())
        val bitmap = (binding.gameFragmentTarget.drawable as BitmapDrawable).bitmap
        val rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        binding.gameFragmentTarget.setImageBitmap(rotatedBitmap)


        val layoutParamsTarget = binding.gameFragmentTarget.layoutParams as ConstraintLayout.LayoutParams

        layoutParamsTarget.width = screenScale.times(gameLevel.targetHeight).toInt()
        layoutParamsTarget.height = screenScale.times(gameLevel.targetHeight.div(2)).toInt()

        layoutParamsTarget.verticalBias = (
                (gameLevel.groundPosition.times(fragmentHeight)).
                minus(gameLevel.elementsPosition.posYTarget.times(screenScale)
                    .plus(gameLevel.targetHeight/2))
                ).div(fragmentHeight).toFloat()

        layoutParamsTarget.horizontalBias = (gameLevel.elementsPosition.posXTarget.plus(gameLevel.targetHeight/2))
            .toFloat()
            .div(gameLevel.widthMeters)
            .toFloat()

        binding.gameFragmentTarget.layoutParams = layoutParamsTarget

    }

    private fun bindEvents() {
        viewModel.currentFragmentWindow.value = FragmentWindow.InputGameWindow

        binding.gameFragmentPlayBotton.setOnClickListener {
            viewModel.update(
                GameLevelAnswer(angle =binding.gameFragmentSlideBarTheta.value.toDouble(),
                    binding.gameFragmentSlideBarVelocity.value.toDouble()), viewModel.clickedMapId.value!!)
                .observe(viewLifecycleOwner) {status->
                    when(status) {
                        is MainViewModel.Status.Failure -> {
                            Log.e("FRAGMENT", "Failed to get gameLevel ", status.e)
                            Snackbar.make(binding.root, "Failed save your inputs for the game", Snackbar.LENGTH_LONG)
                                .show()
                        }
                        MainViewModel.Status.Loading -> {
                            binding.progressHorizontal.visibility = View.VISIBLE
                        }
                        is MainViewModel.Status.Success -> {
                            GameInputWindowFragmentDirections.showGameScene(binding.gameFragmentSlideBarVelocity.value,
                                binding.gameFragmentSlideBarTheta.value)
                                .let {
                                    findNavController().navigate(it)
                                }
                        }
                    }

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
