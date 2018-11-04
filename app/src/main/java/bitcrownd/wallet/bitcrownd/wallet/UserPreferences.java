package bitcrownd.wallet.bitcrownd.wallet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.google.common.base.Joiner;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import bitcrownd.wallet.bitcrownd.BuildConfig;
import bitcrownd.wallet.bitcrownd.R;
import bitcrownd.wallet.bitcrownd.model.BalanceApplication;
import bitcrownd.wallet.bitcrownd.model.WalletConfiguration;
import bitcrownd.wallet.bitcrownd.model.WalletState;
import bitcrownd.wallet.bitcrownd.utils.SecurePreferences;


public class UserPreferences extends PreferenceActivity
{
    public final static String defaultFeesKey = "default_fees";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
            new GeneralPreferences()).commit();
    }

    public static class GeneralPreferences extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(
                this.getActivity().getBaseContext());

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.user_settings);
            PreferenceManager.setDefaultValues(GeneralPreferences.this.getActivity(), R.xml.user_settings, false);

            final EditTextPreference defaultFeeText = (EditTextPreference) findPreference(defaultFeesKey);
            final String currentValue = sharedPrefs.getString(
                defaultFeesKey, getResources().getString(R.string.default_fees));

            defaultFeeText.setSummary(getString(R.string.tab_wallet_bitcoin_count, currentValue));

            final String versionName = BuildConfig.VERSION_NAME;
            final Preference versionPreference = findPreference("version_number");
            versionPreference.setSummary(versionName);

            final Preference seedBackupPreference = findPreference("backup_seed");
            seedBackupPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                // Show the mnemonic for the wallet seed
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    try
                    {
                        if (WalletState.getState().getConfiguration().isPasswordEnabled())
                        {
                            showError(getString(R.string.settings_wallet_seed_error_password_on));
                            return false;
                        }

                        final List<String> mnemonic = MnemonicCode.INSTANCE.toMnemonic(
                            WalletState.getState().getConfiguration().getSeed());

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            GeneralPreferences.this.getActivity());
                        final String fullMnemonic = Joiner.on(" ").join(mnemonic);
                        // Setting Dialog Title
                        alertDialog.setTitle(getString(R.string.settings_wallet_seed_title));
                        alertDialog.setMessage(
                            String.format(getString(R.string.settings_wallet_seed_message), fullMnemonic));

                        alertDialog.setPositiveButton(getString(android.R.string.copy),
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    ClipboardManager clipboard = (ClipboardManager) GeneralPreferences
                                        .this.getActivity().getSystemService(CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("label", fullMnemonic);
                                    clipboard.setPrimaryClip(clip);
                                }
                            });

                        alertDialog.setNegativeButton(
                            getString(R.string.settings_wallet_seed_close),
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.cancel();
                                }
                            });

                        alertDialog.show();
                        return true;
                    }
                    catch (MnemonicException.MnemonicLengthException exception)
                    { }

                    return false;
                }
            });

            ////////777setitings&//////////
            final Preference changesettingsuser = findPreference("user_profile");
            changesettingsuser.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            GeneralPreferences.this.getActivity());

                    final View dialogView = getActivity().getLayoutInflater().inflate(
                            R.layout.activity_login, null);

                    return true;
                }
            });


            final Preference seedRestorePreference = findPreference("restore_seed");
            seedRestorePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    // Display a dialog to restore the wallet seed
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        GeneralPreferences.this.getActivity());

                    final View dialogView = getActivity().getLayoutInflater().inflate(
                        R.layout.dialog_restore_seed, null);

                    alertDialog.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                final EditText text = (EditText) dialogView.findViewById(R.id.mnemonicInput);
                                final String mnemonic = text.getText().toString();

                                String[] words = mnemonic.split(" ");
                                try
                                {
                                    byte[] mnemonicData = MnemonicCode.INSTANCE.toEntropy(Arrays.asList(words));

                                    SecurePreferences preferences = new SecurePreferences(BalanceApplication.getContext());

                                    SecurePreferences.Editor editor = preferences.edit();
                                    editor.putString(WalletState.seedKey, Base64.encodeToString(mnemonicData, Base64.DEFAULT));
                                    editor.commit();

                                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        GeneralPreferences.this.getActivity());
                                    alertDialog.setTitle(getString(R.string.settings_dialog_restore_seed_success_title));
                                    alertDialog.setMessage(getString(R.string.settings_dialog_restore_seed_success_message));
                                    alertDialog.setPositiveButton(getString(android.R.string.ok), null);
                                    alertDialog.show();

                                    return;
                                }
                                catch (MnemonicException.MnemonicLengthException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (MnemonicException.MnemonicWordException e)
                                {
                                    e.printStackTrace();
                                }
                                catch (MnemonicException.MnemonicChecksumException e)
                                {
                                    e.printStackTrace();
                                }

                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    GeneralPreferences.this.getActivity());
                                alertDialog.setTitle(getString(R.string.settings_dialog_restore_seed_error_title));
                                alertDialog.setMessage(getString(R.string.settings_dialog_restore_seed_error_message));
                                alertDialog.setPositiveButton(getString(android.R.string.ok), null);
                                alertDialog.show();
                            }
                        });

                    alertDialog.setNegativeButton(
                        getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();
                            }
                        });

                    alertDialog.setView(dialogView);
                    alertDialog.setTitle(getString(R.string.settings_dialog_restore_seed_title));
                    Dialog restoreDialog = alertDialog.create();
                    restoreDialog.show();

                    return true;
                }
            });

            final Preference walletPassword = findPreference("wallet_password");
            setPasswordSummary();
            walletPassword.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                @Override
                public boolean onPreferenceClick(Preference preference)
                {
                    if (WalletState.getState().getConfiguration().isPasswordEnabled())
                        showDisablePasswordDialog();
                    else
                        showEnablePasswordDialog();

                    return true;
                }
            });
        }

        private void showEnablePasswordDialog()
        {
            // Display a dialog to enable password protection

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GeneralPreferences.this.getActivity());

            final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_enable_password, null);

            alertDialog.setPositiveButton(
                getString(android.R.string.ok),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final EditText edit1 = (EditText) dialogView.findViewById(R.id.password1);
                        String password1 = edit1.getText().toString();
                        final EditText edit2 = (EditText) dialogView.findViewById(R.id.password2);
                        String password2 = edit2.getText().toString();

                        if (password1.equals(password2))
                        {
                            WalletState.getState().getConfiguration().setPassword(password1);
                            setPasswordSummary();
                        }
                        else
                        {
                            showError(getString(R.string.settings_dialog_enable_wallet_password_error));
                        }
                    }
                });

            alertDialog.setNegativeButton(
                getString(android.R.string.cancel),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

            alertDialog.setView(dialogView);
            alertDialog.setTitle(getString(R.string.settings_dialog_enable_wallet_password_title));
            Dialog restoreDialog = alertDialog.create();
            restoreDialog.show();
        }

        private void showDisablePasswordDialog()
        {
            // Display a dialog to disable password protection

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                GeneralPreferences.this.getActivity());

            final View dialogView = getActivity().getLayoutInflater().inflate(
                R.layout.dialog_disable_password, null);

            alertDialog.setPositiveButton(
                getString(android.R.string.ok),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        final EditText edit = (EditText) dialogView.findViewById(R.id.password1);
                        String password = edit.getText().toString();

                        if (WalletState.getState().getConfiguration().comparePassword(password))
                        {
                            WalletState.getState().getConfiguration().setPassword(null);
                            setPasswordSummary();
                        }
                        else
                        {
                            showError(getString(R.string.settings_dialog_disable_wallet_password_error));
                        }
                    }
                });

            alertDialog.setNegativeButton(
                getString(android.R.string.cancel),
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.cancel();
                    }
                });

            alertDialog.setView(dialogView);
            alertDialog.setTitle(getString(R.string.settings_dialog_disable_wallet_password_title));
            Dialog restoreDialog = alertDialog.create();
            restoreDialog.show();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            if ("default_fees".equals(key))
            {
                final EditTextPreference textPreference = (EditTextPreference) findPreference(key);
                final String defaultFees = getResources().getString(R.string.default_fees);
                final String value = sharedPreferences.getString(key, defaultFees);
                try
                {
                    final BigDecimal decimal = new BigDecimal(value);

                    if (decimal.compareTo(BigDecimal.ZERO) >= 0)
                    {
                        textPreference.setSummary(defaultFees + " BTC");
                        return;
                    }
                }
                catch (RuntimeException e)
                { }

                textPreference.setText(value);
                textPreference.setSummary(value + " BTC");
            }
            else if (WalletConfiguration.passwordKey.equals(key))
            {
                setPasswordSummary();
            }
        }

        private void setPasswordSummary()
        {
            final Preference walletPassword = findPreference("wallet_password");

            if (WalletState.getState().getConfiguration().isPasswordEnabled())
                walletPassword.setSummary(getString(R.string.settings_item_summary_wallet_password_enabled));
            else
                walletPassword.setSummary(getString(R.string.settings_item_summary_wallet_password_disabled));
        }

        private void showError(String message)
        {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.getActivity());

            alertDialog.setTitle(getString(R.string.tab_send_error_dialog_title));
            alertDialog.setMessage(message);
            alertDialog.setPositiveButton(getString(android.R.string.ok), null);

            alertDialog.show();
        }
    }
}
