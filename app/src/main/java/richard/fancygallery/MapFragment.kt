package richard.fancygallery


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceManager
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import richard.fancygallery.databinding.FragmentMapBinding
import kotlinx.coroutines.launch

import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {
    private val mapViewModel: MapViewModel by viewModels()

    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMapBinding?= null
    private val binding
        get() = checkNotNull(_binding){"DMBinding is null"}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Configuration.getInstance().load(
            context,
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        with(binding.mapView){
            minZoomLevel = 1.5
            maxZoomLevel = 15.0
            setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
            )
            isVerticalMapRepetitionEnabled = false
            isTilesScaledToDpi = true
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.ALWAYS)
            setTileSource(TileSourceFactory.MAPNIK)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                activityViewModel.galleryItems.collect { giList ->
                    giList.filter { it.latitude != 0.0 && it.longitude != 0.0 }
                        .forEach { galleryItem ->
                            val photoDrawable = loadDrawableFromUrl(galleryItem.url)
                            photoDrawable?.let {
                            val marker = Marker(binding.mapView).apply {
                                position = GeoPoint(galleryItem.latitude, galleryItem.longitude)
                                title = galleryItem.title
                                icon = it
                                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                                relatedObject = galleryItem
                                setOnMarkerClickListener { marker, mapview ->
                                    mapview.apply {
                                        controller.animateTo(marker.position)
                                        overlays.remove(marker)
                                        overlays.add(marker)
                                    }

                                    if (marker.isInfoWindowShown) {
                                        val gItem = marker.relatedObject as GalleryItem
                                        findNavController().navigate(
                                            MapFragmentDirections.showPhotoFromMarker(gItem.photoPageUri)
                                        )
                                    } else {
                                        showInfoWindow()
                                    }
                                    true
                                }
                            }
                            binding.mapView.overlays.add(marker)
                        }
                    }
                    binding.mapView.invalidate()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding.mapView){
            onResume()
            controller.setZoom(mapViewModel.zoomLevel)
            controller.setCenter(mapViewModel.mapCenter)
        }

    }

    override fun onPause() {
        super.onPause()
        with(binding.mapView){
            mapViewModel.saveMapState(zoomLevelDouble, mapCenter)
            onPause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun loadDrawableFromUrl(url: String): Drawable? {
        val loader = ImageLoader(requireContext())
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .build()

        return try {
            val result = loader.execute(request)
            (result as SuccessResult).drawable
        } catch (ex: Exception) {
            null
        }
    }
}