import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';
import { WasteTrackingParametersService } from '../service/waste-tracking-parameters.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './waste-tracking-parameters-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WasteTrackingParametersDeleteDialogComponent {
  wasteTrackingParameters?: IWasteTrackingParameters;

  constructor(protected wasteTrackingParametersService: WasteTrackingParametersService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wasteTrackingParametersService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
