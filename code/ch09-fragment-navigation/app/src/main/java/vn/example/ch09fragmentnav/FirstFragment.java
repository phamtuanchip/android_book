package vn.example.ch09fragmentnav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import vn.example.ch09fragmentnav.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    // Fragment sống lâu hơn View của nó (View bị huỷ/tạo lại nhiều lần trong khi
    // Fragment vẫn còn) — luôn null hoá binding ở onDestroyView để tránh giữ tham
    // chiếu tới View đã chết (memory leak), khác với Activity ở Chương 7.
    private FragmentFirstBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonNext.setOnClickListener(v -> {
            String message = binding.editMessage.getText().toString().trim();
            // Safe Args: FirstFragmentDirections được sinh ra từ nav_graph.xml lúc build,
            // đảm bảo đúng kiểu tham số — gõ sai kiểu sẽ báo lỗi compile, không phải runtime.
            NavDirections action = FirstFragmentDirections.actionFirstToSecond(message);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
