package vn.example.ch09fragmentnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import vn.example.ch09fragmentnav.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // SecondFragmentArgs được sinh ra từ khai báo <argument name="message".../> trong
        // nav_graph.xml — không cần tự đọc/parse Bundle bằng tay như getArguments().getString(...).
        SecondFragmentArgs args = SecondFragmentArgs.fromBundle(requireArguments());
        binding.textReceivedMessage.setText(getString(R.string.received_format, args.getMessage()));

        binding.buttonBack.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
